package edu.cscc.jpaexercise.jpaexercise.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.jpaexercise.jpaexercise.controllers.requests.CreateUserProfileRequest;
import edu.cscc.jpaexercise.jpaexercise.controllers.requests.UserAddressData;
import edu.cscc.jpaexercise.jpaexercise.models.UserProfile;
import edu.cscc.jpaexercise.jpaexercise.repositories.UserProfilesRepository;
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
class UserProfilesControllerTest {

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        userProfilesRepository.deleteAll();
    }

    @Test
    public void postCreatesANewUser() throws Exception {
        List<UserAddressData> userAddresses = List.of(
                new UserAddressData("123 Main St", "Columbus", "OH", "43215"),
                new UserAddressData("456 Elm St", "Columbus", "OH", "43215"),
                new UserAddressData("789 Oak St", "Columbus", "OH", "43215"));
        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest("Jim", "Kirkbride", "jkirkbride@cscc.edu", userAddresses);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createUserProfileRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createUserProfileRequest.firstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createUserProfileRequest.lastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(createUserProfileRequest.email()));

        Optional<UserProfile> userProfile = userProfilesRepository.findAll().stream().findFirst();
        UserProfile foundUserProfile = userProfile.get();
        assertEquals(foundUserProfile.getFirstName(), createUserProfileRequest.firstName());
        assertEquals(foundUserProfile.getLastName(), createUserProfileRequest.lastName());
        assertEquals(foundUserProfile.getEmail(), createUserProfileRequest.email());
    }

    @Test
    public void indexGetsAllUsers() throws Exception {
        UserProfile firstUserProfile = new UserProfile("Hary", "Dresden", "harry.dresden@chicago.net");
        UserProfile secondUserProfile = new UserProfile("Molly", "Carpenter", "molly.carpenter@chicago.net");
        userProfilesRepository.save(firstUserProfile);
        userProfilesRepository.save(secondUserProfile);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        List<UserProfile> userProfiles = objectMapper.readValue(result, new TypeReference<>() {});
        assertEquals(userProfiles.size(), 2);
        assertTrue(userProfiles.stream().anyMatch(user -> user.getFirstName().equals(firstUserProfile.getFirstName())));
        assertTrue(userProfiles.stream().anyMatch(user -> user.getFirstName().equals(secondUserProfile.getFirstName())));
    }
}