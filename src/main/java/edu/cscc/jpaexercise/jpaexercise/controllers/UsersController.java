package edu.cscc.jpaexercise.jpaexercise.controllers;

import edu.cscc.jpaexercise.jpaexercise.controllers.requests.CreateUserRequest;
import edu.cscc.jpaexercise.jpaexercise.models.User;
import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import edu.cscc.jpaexercise.jpaexercise.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        List<User> users = usersRepository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.firstName(), createUserRequest.lastName());
        User finalUser = user;
        List<UserAddress> userAddresses = createUserRequest.userAddresses().stream()
                .map(userAddressData -> new UserAddress(
                        finalUser,
                        userAddressData.street(),
                        userAddressData.city(),
                        userAddressData.state(),
                        userAddressData.zip()))
                .toList();
        user.setUserAddresses(userAddresses);
        user = usersRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
