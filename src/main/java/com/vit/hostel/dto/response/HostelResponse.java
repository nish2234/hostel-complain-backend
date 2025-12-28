package com.vit.hostel.dto.response;

import com.vit.hostel.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelResponse {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private Gender gender;
}
