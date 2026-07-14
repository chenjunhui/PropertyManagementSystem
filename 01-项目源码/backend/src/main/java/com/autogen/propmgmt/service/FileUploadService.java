package com.autogen.propmgmt.service;

import com.autogen.propmgmt.common.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

@Service
public class FileUploadService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/svg+xml",
            "image/pjpeg", "image/x-png"
    );
    private static final Set<String> ALLOWED_EXT = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg");

    private final Path uploadDir;
    private final MessageSource messageSource;

    public FileUploadService(@Value("${app.upload-dir:uploads}") String uploadDirName, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.uploadDir = Paths.get(uploadDirName).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new IllegalStateException("无法创建上传目录", e);
        }
    }

    private String msg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(msg("upload.file_empty"));
        }
        String ext = resolveExtension(file);
        if (ext == null) {
            throw new BusinessException(msg("upload.unsupported_type"));
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException(msg("upload.size_exceeded"));
        }
        String filename = java.util.UUID.randomUUID().toString().replace("-", "") + ext;
        try {
            Files.createDirectories(uploadDir);
            Files.write(uploadDir.resolve(filename), file.getBytes());
        } catch (IOException e) {
            throw new BusinessException(msg("upload.save_failed"));
        }
        return "/api/files/view/" + filename;
    }

    public Path resolveUploadedFile(String filename) {
        if (!StringUtils.hasText(filename) || filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new BusinessException(msg("upload.illegal_path"));
        }
        Path resolved = uploadDir.resolve(filename).normalize();
        if (!resolved.startsWith(uploadDir)) {
            throw new BusinessException(msg("upload.illegal_path"));
        }
        return resolved;
    }

    /** 兼容旧路径 /resources/uploads/xxx 与新路径 /api/files/view/xxx */
    public Path resolveByUrlPath(String urlPath) {
        if (!StringUtils.hasText(urlPath)) {
            throw new BusinessException(msg("upload.path_empty"));
        }
        String filename = urlPath;
        if (filename.startsWith("/api/files/view/")) {
            filename = filename.substring("/api/files/view/".length());
        } else if (filename.startsWith("/resources/uploads/")) {
            filename = filename.substring("/resources/uploads/".length());
        } else if (filename.contains("/")) {
            throw new BusinessException(msg("upload.illegal_path"));
        }
        return resolveUploadedFile(filename);
    }

    private String resolveExtension(MultipartFile file) {
        String original = file.getOriginalFilename();
        String ext = null;
        if (StringUtils.hasText(original) && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.')).toLowerCase(Locale.ROOT);
        }
        String contentType = file.getContentType();
        if (ext != null && ALLOWED_EXT.contains(ext)) {
            if (contentType == null || ALLOWED_TYPES.contains(contentType) || "application/octet-stream".equals(contentType)) {
                return ext;
            }
        }
        if (contentType != null && ALLOWED_TYPES.contains(contentType)) {
            return switch (contentType) {
                case "image/png", "image/x-png" -> ".png";
                case "image/gif" -> ".gif";
                case "image/webp" -> ".webp";
                case "image/svg+xml" -> ".svg";
                default -> ".jpg";
            };
        }
        return null;
    }
}
