package com.vit.hostel.controller;

import com.vit.hostel.dto.request.UpdateComplaintStatusRequest;
import com.vit.hostel.dto.response.ComplaintResponse;
import com.vit.hostel.dto.response.DashboardStatsResponse;
import com.vit.hostel.entity.enums.ComplaintCategory;
import com.vit.hostel.entity.enums.ComplaintStatus;
import com.vit.hostel.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ComplaintService complaintService;

    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) ComplaintStatus status,
            @RequestParam(required = false) ComplaintCategory category) {
        List<ComplaintResponse> complaints = complaintService.getAdminComplaints(
                userDetails.getUsername(), status, category);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/complaints/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ComplaintResponse response = complaintService.getComplaintById(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplaintStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ComplaintResponse response = complaintService.updateComplaintStatus(id, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
            @AuthenticationPrincipal UserDetails userDetails) {
        DashboardStatsResponse stats = complaintService.getDashboardStats(userDetails.getUsername());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dashboard/recent")
    public ResponseEntity<List<ComplaintResponse>> getRecentComplaints(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ComplaintResponse> complaints = complaintService.getRecentComplaints(userDetails.getUsername());
        return ResponseEntity.ok(complaints);
    }
}
