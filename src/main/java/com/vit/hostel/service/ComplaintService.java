package com.vit.hostel.service;

import com.vit.hostel.dto.request.CreateComplaintRequest;
import com.vit.hostel.dto.request.UpdateComplaintStatusRequest;
import com.vit.hostel.dto.response.AttachmentResponse;
import com.vit.hostel.dto.response.ComplaintResponse;
import com.vit.hostel.dto.response.DashboardStatsResponse;
import com.vit.hostel.entity.Attachment;
import com.vit.hostel.entity.Complaint;
import com.vit.hostel.entity.Hostel;
import com.vit.hostel.entity.User;
import com.vit.hostel.entity.enums.ComplaintCategory;
import com.vit.hostel.entity.enums.ComplaintStatus;
import com.vit.hostel.entity.enums.Role;
import com.vit.hostel.exception.BadRequestException;
import com.vit.hostel.exception.ForbiddenException;
import com.vit.hostel.exception.ResourceNotFoundException;
import com.vit.hostel.repository.AttachmentRepository;
import com.vit.hostel.repository.ComplaintRepository;
import com.vit.hostel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public ComplaintResponse createComplaint(CreateComplaintRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Only students can create complaints");
        }

        // Generate ticket number
        String ticketNumber = generateTicketNumber(user.getHostel());

        Complaint complaint = Complaint.builder()
                .ticketNumber(ticketNumber)
                .user(user)
                .hostel(user.getHostel())
                .roomNumber(user.getRoomNumber())
                .category(request.getCategory())
                .description(request.getDescription())
                .status(ComplaintStatus.PENDING)
                .build();

        // Add attachments if provided
        if (request.getAttachmentUrls() != null && !request.getAttachmentUrls().isEmpty()) {
            for (String url : request.getAttachmentUrls()) {
                Attachment attachment = Attachment.builder()
                        .fileUrl(url)
                        .fileName(extractFileName(url))
                        .fileType("image")
                        .build();
                complaint.addAttachment(attachment);
            }
        }

        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    public List<ComplaintResponse> getStudentComplaints(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return complaintRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse getComplaintById(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        // Check access permission
        if (user.getRole() == Role.STUDENT && !complaint.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You don't have access to this complaint");
        }

        if (user.getRole() == Role.ADMIN && !complaint.getHostel().getId().equals(user.getHostel().getId())) {
            throw new ForbiddenException("You don't have access to this complaint");
        }

        return mapToResponse(complaint);
    }

    // Admin methods
    public List<ComplaintResponse> getAdminComplaints(String email, ComplaintStatus status,
            ComplaintCategory category) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Only admins can access this resource");
        }

        List<Complaint> complaints;

        if (status != null) {
            complaints = complaintRepository.findByHostelAndStatusOrderByCreatedAtDesc(admin.getHostel(), status);
        } else if (category != null) {
            complaints = complaintRepository.findByHostelAndCategoryOrderByCreatedAtDesc(admin.getHostel(), category);
        } else {
            complaints = complaintRepository.findByHostelOrderByCreatedAtDesc(admin.getHostel());
        }

        return complaints.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplaintResponse updateComplaintStatus(Long id, UpdateComplaintStatusRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Only admins can update complaint status");
        }

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        // Check if admin has access to this complaint's hostel
        if (!complaint.getHostel().getId().equals(admin.getHostel().getId())) {
            throw new ForbiddenException("You don't have access to this complaint");
        }

        complaint.setStatus(request.getStatus());

        if (request.getAdminRemarks() != null) {
            complaint.setAdminRemarks(request.getAdminRemarks());
        }

        if (request.getResolutionNotes() != null) {
            complaint.setResolutionNotes(request.getResolutionNotes());
        }

        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    public DashboardStatsResponse getDashboardStats(String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Only admins can access dashboard");
        }

        Hostel hostel = admin.getHostel();
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        long total = complaintRepository.countByHostel(hostel);
        long pending = complaintRepository.countByHostelAndStatus(hostel, ComplaintStatus.PENDING);
        long inProgress = complaintRepository.countByHostelAndStatus(hostel, ComplaintStatus.IN_PROGRESS);
        long resolved = complaintRepository.countByHostelAndStatus(hostel, ComplaintStatus.RESOLVED);
        long resolvedThisMonth = complaintRepository.countResolvedThisMonth(hostel, startOfMonth);

        // Category stats
        Map<String, Long> categoryStats = new HashMap<>();
        List<Object[]> categoryData = complaintRepository.countByHostelGroupByCategory(hostel);
        for (Object[] row : categoryData) {
            ComplaintCategory cat = (ComplaintCategory) row[0];
            Long count = (Long) row[1];
            categoryStats.put(cat.getDisplayName(), count);
        }

        // Status stats
        Map<String, Long> statusStats = new HashMap<>();
        List<Object[]> statusData = complaintRepository.countByHostelGroupByStatus(hostel);
        for (Object[] row : statusData) {
            ComplaintStatus stat = (ComplaintStatus) row[0];
            Long count = (Long) row[1];
            statusStats.put(stat.getDisplayName(), count);
        }

        return DashboardStatsResponse.builder()
                .totalComplaints(total)
                .pendingComplaints(pending)
                .inProgressComplaints(inProgress)
                .resolvedComplaints(resolved)
                .resolvedThisMonth(resolvedThisMonth)
                .categoryStats(categoryStats)
                .statusStats(statusStats)
                .build();
    }

    public List<ComplaintResponse> getRecentComplaints(String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Only admins can access this resource");
        }

        return complaintRepository.findTop10ByHostelOrderByCreatedAtDesc(admin.getHostel()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateTicketNumber(Hostel hostel) {
        int year = Year.now().getValue();
        long count = complaintRepository.countByHostelAndYear(hostel, year);
        String hostelCode = hostel.getCode().replace(" ", "").replace("-", "").toUpperCase();
        return String.format("%s-%d-%04d", hostelCode, year, count + 1);
    }

    private String extractFileName(String url) {
        if (url == null)
            return "unknown";
        int lastSlash = url.lastIndexOf('/');
        return lastSlash >= 0 ? url.substring(lastSlash + 1) : url;
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {
        List<AttachmentResponse> attachments = complaint.getAttachments().stream()
                .map(a -> AttachmentResponse.builder()
                        .id(a.getId())
                        .fileName(a.getFileName())
                        .fileUrl(a.getFileUrl())
                        .fileType(a.getFileType())
                        .uploadedAt(a.getUploadedAt())
                        .build())
                .collect(Collectors.toList());

        return ComplaintResponse.builder()
                .id(complaint.getId())
                .ticketNumber(complaint.getTicketNumber())
                .category(complaint.getCategory())
                .categoryDisplayName(complaint.getCategory().getDisplayName())
                .description(complaint.getDescription())
                .status(complaint.getStatus())
                .statusDisplayName(complaint.getStatus().getDisplayName())
                .roomNumber(complaint.getRoomNumber())
                .hostelCode(complaint.getHostel().getCode())
                .hostelName(complaint.getHostel().getName())
                .studentName(complaint.getUser().getName())
                .studentEmail(complaint.getUser().getEmail())
                .adminRemarks(complaint.getAdminRemarks())
                .resolutionNotes(complaint.getResolutionNotes())
                .attachments(attachments)
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .resolvedAt(complaint.getResolvedAt())
                .build();
    }
}
