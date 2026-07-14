package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/** 兼容数据库中旧路径 /resources/uploads/xxx 的访问 */
@RestController
@RequiredArgsConstructor
public class UploadViewController {

    private final FileUploadService fileUploadService;

    @GetMapping("/resources/uploads/{filename:.+}")
    public ResponseEntity<Resource> legacyView(@PathVariable String filename) {
        return serve(fileUploadService.resolveUploadedFile(filename));
    }

    static ResponseEntity<Resource> serve(Path file) {
        try {
            if (!Files.exists(file) || !Files.isRegularFile(file)) {
                return ResponseEntity.notFound().build();
            }
            String contentType = Files.probeContentType(file);
            MediaType mediaType = contentType != null
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic())
                    .body(new FileSystemResource(file));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
