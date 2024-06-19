package edu.cscc.securityexercises.repositories;

import edu.cscc.securityexercises.models.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserProfilesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Test
    @DisplayName("it can retrieve a user")
    public void itCanRetrieveAUser() {
        UserProfile userProfile = new UserProfile("Jim", "kirkbride");
        userProfile = entityManager.persist(userProfile);

        Optional<UserProfile> maybeUser = userProfilesRepository.findById(userProfile.getId());
        assertTrue(maybeUser.isPresent());
        UserProfile foundUserProfile = maybeUser.get();
        assertEquals(userProfile.getFirstName(), foundUserProfile.getFirstName());
        assertEquals(userProfile.getLastName(), foundUserProfile.getLastName());
    }
}