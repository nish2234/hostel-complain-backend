package com.vit.hostel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = ".*@vitstudent\\.ac\\.in$", message = "Email must end with @vitstudent.ac.in")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Hostel code is required")
    private String hostelCode;

    @NotBlank(message = "Room number is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Room number must be 3-4 digits")
    private String roomNumber;
}
