package com.vit.hostel.controller;

import com.vit.hostel.dto.response.HostelResponse;
import com.vit.hostel.entity.enums.Gender;
import com.vit.hostel.service.HostelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
@RequiredArgsConstructor
public class HostelController {

    private final HostelService hostelService;

    @GetMapping
    public ResponseEntity<List<HostelResponse>> getAllHostels(
            @RequestParam(required = false) Gender gender) {
        List<HostelResponse> hostels;
        if (gender != null) {
            hostels = hostelService.getHostelsByGender(gender);
        } else {
            hostels = hostelService.getAllHostels();
        }
        return ResponseEntity.ok(hostels);
    }
}
