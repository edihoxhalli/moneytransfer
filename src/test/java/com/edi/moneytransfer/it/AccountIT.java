package com.edi.moneytransfer.it;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.domain.model.MoneyAmount;
import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import com.edi.moneytransfer.persistence.repository.ReceiptRepository;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReceiptRepository receiptRepository;

    User user;
    Account account;

    @BeforeAll
    public void setup(){
        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        userRepository.save(user);

        account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
    }

    @Test
    @WithMockUser(username = "username", authorities = { "USER" })
    @DisplayName("Successfully updated account balance of mock user")
    public void topUpAccount_SucessfullyUpdatesAccountBalance_Test() throws Exception{
        BigDecimal balance = new BigDecimal(1000);
        MoneyAmount amount = new MoneyAmount(balance);

        AccountDto response =
                mapper.readValue(mockMvc
                                .perform(put("/account")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(amount)))
                                .andExpect(status().is2xxSuccessful())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        AccountDto.class);


        assertThat(response.getBalance().equals(balance));

        Account accountStored = accountRepository.findByUser(user);
        assertThat(accountStored.getBalance().equals(balance));

        assertThat(receiptRepository.findByAccount(accountStored).size() == 1);

    }

    @Test
    @WithMockUser(username = "username", authorities = { "USER" })
    @DisplayName("Returns bad request when amount is negative")
    public void topUpAccountNegativeAmount_ReturnsBadRequest_Test() throws Exception{
        BigDecimal balance = new BigDecimal(-1000);
        MoneyAmount amount = new MoneyAmount(balance);

        mockMvc
            .perform(put("/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(amount)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns forbidden status code when requested without authenticated mock user")
    public void topUpAccountWithoutMockUser_ReturnsForbidden_Test() throws Exception{
        BigDecimal balance = new BigDecimal(1000);
        MoneyAmount amount = new MoneyAmount(balance);

        mockMvc
                .perform(put("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(amount)))
                .andExpect(status().isForbidden());
    }
}
