package edu.cscc.jpaexercise.jpaexercise.controllers;

import edu.cscc.jpaexercise.jpaexercise.controllers.requests.CreateUserProfileRequest;
import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import edu.cscc.jpaexercise.jpaexercise.models.UserProfile;
import edu.cscc.jpaexercise.jpaexercise.repositories.UserAddressesRepository;
import edu.cscc.jpaexercise.jpaexercise.repositories.UserProfilesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserProfilesController {

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        List<UserProfile> userProfiles = userProfilesRepository.findAll();

        return new ResponseEntity<>(userProfiles, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createUserProfile(@RequestBody @Valid CreateUserProfileRequest createUserProfileRequest) {
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
