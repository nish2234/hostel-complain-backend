package com.vit.hostel.dto.request;

import com.vit.hostel.entity.enums.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateComplaintRequest {

    @NotNull(message = "Category is required")
    private ComplaintCategory category;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    private List<String> attachmentUrls;
}
