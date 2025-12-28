package com.vit.hostel.dto.response;

import com.vit.hostel.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private Role role;
    private String hostelCode;
    private String hostelName;
    private String roomNumber;
}
