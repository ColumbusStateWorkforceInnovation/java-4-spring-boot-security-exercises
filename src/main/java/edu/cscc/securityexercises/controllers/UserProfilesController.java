package edu.cscc.securityexercises.controllers;

import edu.cscc.securityexercises.controllers.requests.CreateUserProfileRequest;
import edu.cscc.securityexercises.models.UserAddress;
import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.repositories.UserAddressesRepository;
import edu.cscc.securityexercises.repositories.UserProfilesRepository;
import edu.cscc.securityexercises.services.UserProfilesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserProfilesController {

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Autowired
    private UserProfilesService userProfilesService;

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @PostMapping()
    public ResponseEntity<?> createUserProfile(@RequestBody @Valid CreateUserProfileRequest createUserProfileRequest) {
        UserProfile userProfile = userProfilesService.create(createUserProfileRequest);
        List<UserAddress> userAddresses = createUserProfileRequest.userAddresses().stream()
                .map(userAddressData -> new UserAddress(
                        userProfile,
                        userAddressData.street(),
                        userAddressData.city(),
                        userAddressData.state(),
                        userAddressData.zip()))
                .toList();

        userAddressesRepository.saveAll(userAddresses);

        return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
    }
}
