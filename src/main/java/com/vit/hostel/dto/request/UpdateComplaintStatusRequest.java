package com.vit.hostel.dto.request;

import com.vit.hostel.entity.enums.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateComplaintStatusRequest {

    @NotNull(message = "Status is required")
    private ComplaintStatus status;

    @Size(max = 1000, message = "Admin remarks must be less than 1000 characters")
    private String adminRemarks;

    @Size(max = 2000, message = "Resolution notes must be less than 2000 characters")
    private String resolutionNotes;
}
