package edu.cscc.jpaexercise.jpaexercise.controllers.requests;

public record UserAddressData(
        String street,
        String city,
        String state,
        String zip
) {
}
