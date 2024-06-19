package edu.cscc.jpaexercise.jpaexercise.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.jpaexercise.jpaexercise.controllers.requests.CreateUserRequest;
import edu.cscc.jpaexercise.jpaexercise.controllers.requests.UserAddressData;
import edu.cscc.jpaexercise.jpaexercise.models.User;
import edu.cscc.jpaexercise.jpaexercise.models.UserAddress;
import edu.cscc.jpaexercise.jpaexercise.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.test.properties"
)
class UsersControllerTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        usersRepository.deleteAll();
    }

    @Test
    public void postCreatesANewUser() throws Exception {
        List<UserAddressData> userAddresses = List.of(
                new UserAddressData("123 Main St", "Columbus", "OH", "43215"),
                new UserAddressData("456 Elm St", "Columbus", "OH", "43215"),
                new UserAddressData("789 Oak St", "Columbus", "OH", "43215"));
        CreateUserRequest createUserRequest = new CreateUserRequest("Jim", "Kirkbride", userAddresses);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createUserRequest.firstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createUserRequest.lastName()));

        Optional<User> user = usersRepository.findAll().stream().findFirst();
        User foundUser = user.get();
        assertEquals(foundUser.getFirstName(), createUserRequest.firstName());
        assertEquals(foundUser.getLastName(), createUserRequest.lastName());
    }

    @Test
    public void indexGetsAllUsers() throws Exception {
        User firstUser = new User("Hary", "Dresden");
        User secondUser = new User("Molly", "Carpenter");
        usersRepository.save(firstUser);
        usersRepository.save(secondUser);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        List<User> users = objectMapper.readValue(result, new TypeReference<>() {});
        assertEquals(users.size(), 2);
        assertTrue(users.stream().anyMatch(user -> user.getFirstName().equals(firstUser.getFirstName())));
        assertTrue(users.stream().anyMatch(user -> user.getFirstName().equals(secondUser.getFirstName())));
    }
}