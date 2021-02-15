package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
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
}
