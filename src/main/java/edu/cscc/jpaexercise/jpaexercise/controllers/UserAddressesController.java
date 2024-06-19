package edu.cscc.jpaexercise.jpaexercise.controllers;

import edu.cscc.jpaexercise.jpaexercise.controllers.requests.CreateUserAddressRequest;
import edu.cscc.jpaexercise.jpaexercise.exceptions.ResourceNotFoundException;
import edu.cscc.jpaexercise.jpaexercise.models.UserProfile;
import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import edu.cscc.jpaexercise.jpaexercise.repositories.UserAddressesRepository;
import edu.cscc.jpaexercise.jpaexercise.repositories.UserProfilesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user-addresses")
public class UserAddressesController {

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserAddressRequest createUserAddressRequest) {
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
