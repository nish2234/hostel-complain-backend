package com.vit.hostel.controller;

import com.vit.hostel.dto.request.CreateComplaintRequest;
import com.vit.hostel.dto.response.ComplaintResponse;
import com.vit.hostel.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final ComplaintService complaintService;

    @PostMapping("/complaints")
    public ResponseEntity<ComplaintResponse> createComplaint(
            @Valid @RequestBody CreateComplaintRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ComplaintResponse response = complaintService.createComplaint(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ComplaintResponse> complaints = complaintService.getStudentComplaints(userDetails.getUsername());
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/complaints/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ComplaintResponse response = complaintService.getComplaintById(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
