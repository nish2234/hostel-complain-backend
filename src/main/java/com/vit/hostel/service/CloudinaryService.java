package com.vit.hostel.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vit.hostel.dto.response.UploadResponse;
import com.vit.hostel.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public UploadResponse uploadFile(MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }

            // Validate file type (images only)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BadRequestException("Only image files are allowed");
            }

            // Validate file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new BadRequestException("File size must be less than 10MB");
            }

            // Upload to Cloudinary
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "hostel-complaints",
                    "resource_type", "image"));

            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return UploadResponse.builder()
                    .url(url)
                    .publicId(publicId)
                    .fileName(file.getOriginalFilename())
                    .build();

        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary", e);
            throw new BadRequestException("Failed to upload file: " + e.getMessage());
        }
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.error("Error deleting file from Cloudinary", e);
        }
    }
}
