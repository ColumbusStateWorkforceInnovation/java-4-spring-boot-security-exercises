package edu.cscc.jpaexercise.jpaexercise.repositories;

import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserAddressesRepository extends JpaRepository<UserAddress, Integer> {
}
