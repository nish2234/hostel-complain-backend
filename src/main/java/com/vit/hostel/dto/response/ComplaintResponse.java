package com.vit.hostel.dto.response;

import com.vit.hostel.entity.enums.ComplaintCategory;
import com.vit.hostel.entity.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {
    private Long id;
    private String ticketNumber;
    private ComplaintCategory category;
    private String categoryDisplayName;
    private String description;
    private ComplaintStatus status;
    private String statusDisplayName;
    private String roomNumber;
    private String hostelCode;
    private String hostelName;
    private String studentName;
    private String studentEmail;
    private String adminRemarks;
    private String resolutionNotes;
    private List<AttachmentResponse> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
}
