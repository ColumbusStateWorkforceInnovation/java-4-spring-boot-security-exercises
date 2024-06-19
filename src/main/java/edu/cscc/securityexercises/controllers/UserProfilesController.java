package edu.cscc.securityexercises.controllers;

import edu.cscc.securityexercises.controllers.requests.CreateUserProfileRequest;
import edu.cscc.securityexercises.models.UserAddress;
import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.repositories.UserAddressesRepository;
import edu.cscc.securityexercises.repositories.UserProfilesRepository;
import edu.cscc.securityexercises.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserProfilesController {

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping()
    @PreAuthorize("@tokenService.hasScope(#jwt, 'profiles:create')")
    public ResponseEntity<?> createUserProfile(
            @RequestBody @Valid CreateUserProfileRequest createUserProfileRequest,
            @AuthenticationPrincipal Jwt jwt) {
        UserProfile userProfile = new UserProfile(createUserProfileRequest.firstName(), createUserProfileRequest.lastName(), createUserProfileRequest.email());
        UserProfile finalUserProfile = userProfile;
        userProfile = userProfilesRepository.save(userProfile);
        List<UserAddress> userAddresses = createUserProfileRequest.userAddresses().stream()
                .map(userAddressData -> new UserAddress(
                        finalUserProfile,
                        userAddressData.street(),
                        userAddressData.city(),
                        userAddressData.state(),
                        userAddressData.zip()))
                .toList();

        userAddressesRepository.saveAll(userAddresses);

        return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
    }
}
