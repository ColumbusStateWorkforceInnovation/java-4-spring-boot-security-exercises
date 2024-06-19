package edu.cscc.jpaexercise.jpaexercise.controllers.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserAddressRequest(
        @NotNull Integer userId,
        @NotEmpty String street,
        @NotEmpty String city,
        @NotEmpty @Size(min = 2, max = 2) String state,
        @NotEmpty @Size(min= 5, max = 5) String zip
) {
}
