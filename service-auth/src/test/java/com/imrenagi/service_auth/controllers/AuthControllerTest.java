package com.imrenagi.service_auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imrenagi.service_auth.AuthApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

/**
 * Created by imrenagi on 5/14/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AuthApplication.class)
@WebAppConfiguration
public class AuthControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private AuthController authController;

    @Mock
    private ConsumerTokenServices tokenServices;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void logoutWitTokenShouldCallRevokeToken() throws Exception {
        mockMvc.perform(get("/oauth/logout").header("Authorization", "Bearer 123434234235"))
                .andExpect(status().isOk());
        verify(tokenServices, times(1)).revokeToken("123434234235");
    }

    @Test
    public void logoutWithoutTokenShouldDoNothing() throws Exception {
        mockMvc.perform(get("/oauth/logout").header("Authorization", "Bearer "))
                .andExpect(status().isOk());
        verify(tokenServices, times(0)).revokeToken(anyString());
    }

}