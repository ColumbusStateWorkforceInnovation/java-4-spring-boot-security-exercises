package edu.cscc.jpaexercise.jpaexercise.repositories;

import edu.cscc.jpaexercise.jpaexercise.models.User;
import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findByUserAddresses(UserAddress userAddress);
}
