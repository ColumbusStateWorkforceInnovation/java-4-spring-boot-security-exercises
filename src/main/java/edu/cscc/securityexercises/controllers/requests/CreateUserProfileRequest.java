package edu.cscc.securityexercises.controllers.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateUserProfileRequest(
        @NotEmpty String firstName,
        @NotEmpty String lastName,
        @NotEmpty @Email String email,
        @NotEmpty String password,
        List<UserAddressData> userAddresses
) { }
