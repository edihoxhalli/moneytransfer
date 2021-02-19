package com.edi.moneytransfer.it;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User successfully created")
    public void createUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "username", "password", null);

        UserDto response =
                mapper.readValue(mockMvc
                .perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString(),
                        UserDto.class);


        User userStored = userRepository.findByUsername(userDto.getUsername());
        assertEquals(response.getId(), userStored.getId());
        assertEquals(response.getPassword(), userStored.getPassword());
        assertEquals(response.getUsername(), userStored.getUsername());
        assertEquals(response.getAccount().getId(), userStored.getAccount().getId());

    }

    @Test
    @DisplayName("User creation fails if password is empty")
    public void createUserWithEmptyPasswordFailsTest() throws Exception {
        UserDto userDto = new UserDto(null, "username", "", null);
        mockMvc
            .perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(userDto)))
            .andExpect(status().is4xxClientError());

        assertEquals(null, userRepository.findByUsername(userDto.getUsername()));

    }

    @Test
    @DisplayName("User can login successfully")
    public void userLoginSuccessfulTest() throws Exception {
        UserDto userDto = new UserDto(null, "username", "password", null);
        mockMvc
                .perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().is2xxSuccessful());


        String sessionName = "sessionName";
        String sessionValue = "sessionValue";

        //login
        HttpSession loginSession = mockMvc
                .perform(put("/user")
                        .sessionAttr(sessionName, sessionValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getRequest()
                .getSession();


        assertNotNull(loginSession);


        UserDto response =
                mapper.readValue(mockMvc
                                .perform(get("/user")
                                        .session((MockHttpSession) loginSession)
                                )
                                .andExpect(status().is2xxSuccessful())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        UserDto.class);

        assertEquals(userDto.getUsername(), response.getUsername());

    }

    @Test
    @DisplayName("Get current user returns null if not authenticated")
    public void getCurrentUserReturnsNullIfNotAuthenticated_Test() throws Exception{
        String response = mockMvc
                            .perform(get("/user")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().is2xxSuccessful())
                            .andReturn()
                            .getRequest()
                            .getContentAsString();
        assertEquals(null, response);
    }
}
