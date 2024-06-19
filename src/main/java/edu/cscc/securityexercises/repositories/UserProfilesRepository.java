package edu.cscc.securityexercises.repositories;

import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserProfilesRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUserAddresses(UserAddress userAddress);
}
