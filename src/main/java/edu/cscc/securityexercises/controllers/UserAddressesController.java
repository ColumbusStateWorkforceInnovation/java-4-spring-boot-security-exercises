package edu.cscc.securityexercises.controllers;

import edu.cscc.securityexercises.controllers.requests.CreateUserAddressRequest;
import edu.cscc.securityexercises.exceptions.ResourceNotFoundException;
import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.models.UserAddress;
import edu.cscc.securityexercises.repositories.UserAddressesRepository;
import edu.cscc.securityexercises.repositories.UserProfilesRepository;
import edu.cscc.securityexercises.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user-addresses")
public class UserAddressesController {

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping()
    @PreAuthorize("@tokenService.hasScopes(#jwt, 'user_addresses:create profiles:read')")
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateUserAddressRequest createUserAddressRequest,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Optional<UserProfile> user = userProfilesRepository.findById(createUserAddressRequest.userId());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        UserAddress userAddress = new UserAddress(
                user.get(),
                createUserAddressRequest.street(),
                createUserAddressRequest.city(),
                createUserAddressRequest.state(),
                createUserAddressRequest.zip()
        );
        UserAddress newUserAddress = userAddressesRepository.save(userAddress);
        return ResponseEntity.ok(newUserAddress);
    }
}
