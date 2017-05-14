package com.imrenagi.service_account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imrenagi.service_account.AccountApplication;
import com.imrenagi.service_account.domains.Account;
import com.imrenagi.service_account.domains.User;
import com.imrenagi.service_account.service.AccountService;
import com.sun.security.auth.UserPrincipal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by imrenagi on 5/13/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountApplication.class)
@WebAppConfiguration
public class AccountControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldGetCurrentAccount() throws Exception {
        final Account account = new Account();
        account.setUsername("test");

        when(accountService.findByUserName(account.getUsername())).thenReturn(account);

        mockMvc.perform(get("/current").principal(new UserPrincipal(account.getUsername())))
                .andExpect(jsonPath("$.username").value(account.getUsername()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAccountByName() throws Exception {
        final Account account = new Account();
        account.setUsername("test");

        when(accountService.findByUserName(account.getUsername())).thenReturn(account);

        mockMvc.perform(get("/"+account.getUsername()))
                .andExpect(jsonPath("$.username").value(account.getUsername()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateNewAccount() throws Exception {
        final User user = new User();
        user.setUsername("imrenagi");
        user.setPassword("imrenagi");
        user.setFirstName("imre");
        user.setEmail("imrenagi@gmail.com");

        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFailOnValidationTryingToCreateNewAccount() throws Exception {
        final User user = new User();
        user.setUsername("imrenagi");
        user.setPassword("imrenagi");

        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

}