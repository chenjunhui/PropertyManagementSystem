package com.autogen.propmgmt.controller;

import com.autogen.propmgmt.common.Result;
import com.autogen.propmgmt.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.upload(file);
        return Result.ok(Map.of("url", url));
    }

    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> view(@PathVariable String filename) {
        return UploadViewController.serve(fileUploadService.resolveUploadedFile(filename));
    }
}
