package edu.cscc.securityexercises.services;

import edu.cscc.securityexercises.controllers.requests.CreateUserProfileRequest;
import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.repositories.UserProfilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserProfilesService implements UserDetailsService {

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    public UserProfile create(CreateUserProfileRequest createUserProfileRequest) {
        UserProfile userProfile = new UserProfile(
                createUserProfileRequest.firstName(),
                createUserProfileRequest.lastName(),
                createUserProfileRequest.email()
        );

        // Encrypt the password before saving it
        String encryptedPassword =
                new BCryptPasswordEncoder()
                        .encode(createUserProfileRequest.password());

        userProfile.setPassword(encryptedPassword);

        return userProfilesRepository.save(userProfile);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserProfile userProfile = userProfilesRepository.findByEmail(email);
        return User.withUsername(userProfile.getEmail())
                .password(userProfile.getPassword())
                .build();
    }
}
