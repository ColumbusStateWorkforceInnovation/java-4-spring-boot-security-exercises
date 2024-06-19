package edu.cscc.securityexercises.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.securityexercises.controllers.requests.CreateUserProfileRequest;
import edu.cscc.securityexercises.controllers.requests.UserAddressData;
import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.repositories.UserProfilesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                        .content(objectMapper.writeValueAsString(createUserProfileRequest))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt((jwt) -> {
                            jwt.claims(claims -> {
                                claims.put("scope", "profiles:create");
                            });
                        })))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createUserProfileRequest.firstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createUserProfileRequest.lastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(createUserProfileRequest.email()));

        Optional<UserProfile> userProfile = userProfilesRepository.findByEmail(createUserProfileRequest.email());
        UserProfile foundUserProfile = userProfile.get();
        assertEquals(foundUserProfile.getFirstName(), createUserProfileRequest.firstName());
        assertEquals(foundUserProfile.getLastName(), createUserProfileRequest.lastName());
        assertEquals(foundUserProfile.getEmail(), createUserProfileRequest.email());
    }
}