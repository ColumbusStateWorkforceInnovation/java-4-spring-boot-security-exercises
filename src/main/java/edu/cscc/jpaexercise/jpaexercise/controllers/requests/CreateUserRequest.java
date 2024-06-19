package edu.cscc.jpaexercise.jpaexercise.controllers.requests;

import java.util.List;

public record CreateUserRequest(
        String firstName,
        String lastName,
        List<UserAddressData> userAddresses
) { }
