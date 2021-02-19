package com.edi.moneytransfer.it;

import com.edi.moneytransfer.application.dto.AccountDto;
import com.edi.moneytransfer.application.dto.TransferDto;
import com.edi.moneytransfer.domain.exception.InsufficientFundsException;
import com.edi.moneytransfer.domain.exception.TransferToSelfException;
import com.edi.moneytransfer.persistence.entity.Account;
import com.edi.moneytransfer.persistence.entity.Transfer;
import com.edi.moneytransfer.persistence.entity.User;
import com.edi.moneytransfer.persistence.repository.AccountRepository;
import com.edi.moneytransfer.persistence.repository.TransferRepository;
import com.edi.moneytransfer.persistence.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransferIntegrationTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    UserRepository userRepository;


    User user1;
    Account account1;
    User user2;
    Account account2;

    @BeforeEach
    public void setup(){
        user1 = new User();
        user1.setUsername("fernando");
        user1.setPassword("password");
        userRepository.save(user1);

        account1 = new Account();
        account1.setUser(user1);
        account1.setBalance(new BigDecimal("0.00"));
        accountRepository.save(account1);

        user2 = new User();
        user2.setUsername("edi");
        user2.setPassword("password");
        userRepository.save(user2);

        account2 = new Account();
        account2.setUser(user2);
        account2.setBalance(new BigDecimal("5000.00"));
        accountRepository.save(account2);
    }

    @Test
    @WithMockUser(username = "edi")
    @DisplayName("User transfers successfully when conditions are met")
    public void transferSuccessfulWhenSenderBalanceSufficient_Test() throws Exception {
        BigDecimal transferAmount = new BigDecimal("4000.01");
        TransferDto transferDto = new TransferDto(null, "fernando", transferAmount);
        AccountDto response =
                mapper.readValue(mockMvc
                                .perform(post("/transfer")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(mapper.writeValueAsString(transferDto)))
                                .andExpect(status().is2xxSuccessful())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        AccountDto.class);


        BigDecimal expectedRemainingBalanceUser2 = new BigDecimal("999.99");

        assertEquals(expectedRemainingBalanceUser2, response.getBalance());

        Account recipientAccount = accountRepository.findByUser(user1);
        Account senderAccount = accountRepository.findByUser(user2);

        assertEquals(expectedRemainingBalanceUser2, senderAccount.getBalance());
        assertEquals(transferAmount, recipientAccount.getBalance());

        List<Transfer> transferList = transferRepository.findBySenderAndRecipient(senderAccount, recipientAccount);
        assertEquals(transferList.size(), 1);
        assertEquals(transferList.get(0).getAmount(), transferAmount);
    }

    @Test
    @WithMockUser(username = "edi")
    @DisplayName("User cannot transfer negative amount")
    public void get400BadRequestWhenUserTransfersNegativeAmount_Test() throws Exception {
        BigDecimal transferAmount = new BigDecimal(-100);
        TransferDto transferDto = new TransferDto(null, "fernando", transferAmount);
        mockMvc
            .perform(post("/transfer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(transferDto)))
            .andExpect(status().isBadRequest());

        Account recipientAccount = accountRepository.findByUser(user1);
        Account senderAccount = accountRepository.findByUser(user2);

        assertEquals(account1.getBalance(), recipientAccount.getBalance());
        assertEquals(account2.getBalance(), senderAccount.getBalance());
    }

    @Test
    @WithMockUser(username = "edi")
    @DisplayName("User cannot transfer money to self")
    public void return400BadRequestWhenUserTransfersToSelf_Test() throws Exception {
        BigDecimal transferAmount = new BigDecimal(100);
        TransferDto transferDto = new TransferDto(null, "edi", transferAmount);
        mockMvc
                .perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transferDto)))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof TransferToSelfException));

        Account senderAccount = accountRepository.findByUser(user2);

        assertEquals(account2.getBalance(), senderAccount.getBalance());
    }

    @Test
    @WithMockUser(username = "edi")
    @DisplayName("User cannot transfer money to user that doesn't exist")
    public void return400BadRequestWhenUserTransfersToInexistingUser_Test() throws Exception {
        BigDecimal transferAmount = new BigDecimal(-100);
        TransferDto transferDto = new TransferDto(null, "inexisting", transferAmount);
        mockMvc
                .perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transferDto)))
                .andExpect(status().isBadRequest());

        Account recipientAccount = accountRepository.findByUser(user1);
        Account senderAccount = accountRepository.findByUser(user2);

        assertEquals(account1.getBalance(), recipientAccount.getBalance());
        assertEquals(account2.getBalance(), senderAccount.getBalance());
    }

    @Test
    @WithMockUser(username = "edi")
    @DisplayName("User cannot transfer greater amount of money than his current balance")
    public void return400BadRequestWhenInsufficientFunds_Test() throws Exception {
        BigDecimal transferAmount = new BigDecimal(90000);
        TransferDto transferDto = new TransferDto(null, "fernando", transferAmount);
        mockMvc
                .perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transferDto)))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof InsufficientFundsException));

        Account recipientAccount = accountRepository.findByUser(user1);
        Account senderAccount = accountRepository.findByUser(user2);

        assertEquals(account1.getBalance(), recipientAccount.getBalance());
        assertEquals(account2.getBalance(), senderAccount.getBalance());
    }
}
