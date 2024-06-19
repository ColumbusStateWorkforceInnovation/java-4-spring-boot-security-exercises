package edu.cscc.jpaexercise.jpaexercise.repositories;

import edu.cscc.jpaexercise.jpaexercise.models.User;
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
class UsersRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("it can retrieve a user")
    public void itCanRetrieveAUser() {
        User user = new User("Jim", "kirkbride");
        user = entityManager.persist(user);

        Optional<User> maybeUser = usersRepository.findById(user.getId());
        assertTrue(maybeUser.isPresent());
        User foundUser = maybeUser.get();
        assertEquals(user.getFirstName(), foundUser.getFirstName());
        assertEquals(user.getLastName(), foundUser.getLastName());
    }
}