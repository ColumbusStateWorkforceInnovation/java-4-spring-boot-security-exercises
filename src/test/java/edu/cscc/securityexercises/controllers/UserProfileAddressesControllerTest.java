package edu.cscc.securityexercises.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.securityexercises.controllers.requests.CreateUserAddressRequest;
import edu.cscc.securityexercises.models.UserProfile;
import edu.cscc.securityexercises.repositories.UserAddressesRepository;
import edu.cscc.securityexercises.repositories.UserProfilesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.test.properties"
)
class UserProfileAddressesControllerTest {

    @Autowired
    private UserProfilesRepository userProfilesRepository;

    @Autowired
    private UserAddressesRepository userAddressesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        userAddressesRepository.deleteAll();
        userProfilesRepository.deleteAll();
    }

    @Test
    @DisplayName("It can create a user address")
    public void itCanCreateAUserAddress() throws Exception {
        UserProfile userProfile = userProfilesRepository.save(new UserProfile("Your", "Name", "your.name@gmail.com"));
        CreateUserAddressRequest createUserAddressRequest =
                new CreateUserAddressRequest(userProfile.getId(), "123 Main St", "Columbus", "OH", "43215");

        mockMvc.perform(MockMvcRequestBuilders.post("/user-addresses")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createUserAddressRequest))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt((jwt) -> {
                            jwt.claims(claims -> {
                                claims.put("scope", "profiles:read user_addresses:create");
                            });
                        })))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(createUserAddressRequest.street()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(createUserAddressRequest.city()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(createUserAddressRequest.state()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zip").value(createUserAddressRequest.zip()))
                ;
    }

    @Test
    @DisplayName("It returns a 404 when the user is not found")
    public void itReturnsA404WhenTheUserIsNotFound() throws Exception {
        CreateUserAddressRequest createUserAddressRequest =
                new CreateUserAddressRequest(999, "123 Main St", "Columbus", "OH", "43215");

        mockMvc.perform(MockMvcRequestBuilders.post("/user-addresses")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createUserAddressRequest))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt((jwt) -> {
                            jwt.claims(claims -> {
                                claims.put("scope", "profiles:read user_addresses:create");
                            });
                        })))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("It returns a 400 when the user address request is invalid")
    public void itReturnsA400WhenTheUserAddressRequestIsInvalid() throws Exception {
        CreateUserAddressRequest createUserAddressRequest =
                new CreateUserAddressRequest(null, "", null, "Ohio", "43");

        mockMvc.perform(MockMvcRequestBuilders.post("/user-addresses")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createUserAddressRequest))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt((jwt) -> {
                            jwt.claims(claims -> {
                                claims.put("scope", "profiles:read user_addresses:create");
                            });
                        })))
                .andExpect(status().isBadRequest());
    }
}