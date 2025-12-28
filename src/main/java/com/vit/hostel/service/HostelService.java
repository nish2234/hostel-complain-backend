package com.vit.hostel.service;

import com.vit.hostel.dto.response.HostelResponse;
import com.vit.hostel.entity.Hostel;
import com.vit.hostel.entity.enums.Gender;
import com.vit.hostel.repository.HostelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostelService {

    private final HostelRepository hostelRepository;

    public List<HostelResponse> getAllHostels() {
        return hostelRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<HostelResponse> getHostelsByGender(Gender gender) {
        return hostelRepository.findByGender(gender).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HostelResponse mapToResponse(Hostel hostel) {
        return HostelResponse.builder()
                .id(hostel.getId())
                .code(hostel.getCode())
                .name(hostel.getName())
                .phone(hostel.getPhone())
                .gender(hostel.getGender())
                .build();
    }
}
