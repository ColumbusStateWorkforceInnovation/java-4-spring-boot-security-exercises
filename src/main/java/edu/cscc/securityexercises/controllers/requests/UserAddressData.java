package edu.cscc.securityexercises.controllers.requests;

public record UserAddressData(
        String street,
        String city,
        String state,
        String zip
) {
}
