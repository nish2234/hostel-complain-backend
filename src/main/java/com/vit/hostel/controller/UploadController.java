package com.vit.hostel.controller;

import com.vit.hostel.dto.response.UploadResponse;
import com.vit.hostel.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        UploadResponse response = cloudinaryService.uploadFile(file);
        return ResponseEntity.ok(response);
    }
}
