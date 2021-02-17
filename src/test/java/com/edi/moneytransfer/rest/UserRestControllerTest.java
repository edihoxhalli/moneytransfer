package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Test
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
        assertThat(userStored.getId().equals(response.getId()));
        assertThat(userStored.getPassword().equals(response.getPassword()));
        assertThat(userStored.getUsername().equals(response.getUsername()));
        assertThat(userStored.getAccount().getId().equals(response.getAccount().getId()));

    }

    @Test
    public void createUserWithEmptyPasswordFailsTest() throws Exception {
        UserDto userDto = new UserDto(null, "username", "", null);
        mockMvc
            .perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(userDto)))
            .andExpect(status().is4xxClientError());

        assertThat(userRepository.findByUsername(userDto.getUsername()) == null);

    }

    @Test
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

        assertThat(response.getUsername().equals(userDto.getUsername()));

    }
}
