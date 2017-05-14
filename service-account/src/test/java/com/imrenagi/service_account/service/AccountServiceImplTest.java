package com.imrenagi.service_account.service;

import com.imrenagi.service_account.AccountApplication;
import com.imrenagi.service_account.client.AuthServiceClient;
import com.imrenagi.service_account.domains.Account;
import com.imrenagi.service_account.domains.User;
import com.imrenagi.service_account.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by imrenagi on 5/14/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountApplication.class)
@WebAppConfiguration
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AuthServiceClient authClient;

    @Mock
    private AccountRepository repository;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldFindByUserName() {
        final Account account = new Account();
        account.setUsername("test");

        doReturn(account).when(repository).findByUsername(account.getUsername());
        Account found = accountService.findByUserName(account.getUsername());

        assertEquals(account, found);
        verify(repository, times(1)).findByUsername(account.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailFindByUserNameForEmptyUsername() {
        accountService.findByUserName("");
    }

    @Test
    public void shouldFindByEmail() {
        final Account account = new Account();
        account.setUsername("imrenagi");
        account.setEmail("imre.nagi2812@gmail.com");

        doReturn(account).when(repository).findByEmail(account.getEmail());
        Account found = accountService.findByEmail(account.getEmail());

        assertEquals(account, found);
        verify(repository, times(1)).findByEmail(account.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFindByEmptyEmail() {
        accountService.findByEmail("");
    }

    @Test
    public void shouldCreateNewAccountWithGivenCompleteUser() {
        User user = getStubUser();

        Account mockAccount = new Account();
        mockAccount.setUsername(user.getUsername());
        mockAccount.setEmail(user.getEmail());
        mockAccount.setFirstName(user.getFirstName());
        mockAccount.setLastName(user.getLastName());
        mockAccount.setAddress(user.getAddress());
        mockAccount.setPhoneNumber(user.getPhoneNumber());
        mockAccount.setCreatedAt(new Date());
        mockAccount.setUpdatedAt(new Date());

        doReturn(null).when(repository).findByUsername(user.getUsername());
        doReturn(mockAccount).when(repository).save(any(Account.class));

        Account account = accountService.create(user);

        assertEquals(user.getUsername(), account.getUsername());
        assertEquals(user.getFirstName(), account.getFirstName());
        assertEquals(user.getLastName(), account.getLastName());
        assertNotNull(account.getCreatedAt());
        assertNotNull(account.getUpdatedAt());
        assertNull(account.getDeletedAt());

        verify(authClient, times(1)).createUser(user);
        verify(repository, times(1)).save(any(Account.class));
        verify(repository, times(1)).findByUsername(user.getUsername());
        verify(repository, times(0)).findByEmail(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfUserIsExist() {
        doReturn(new Account()).when(repository).findByUsername(anyString());
        accountService.create(getStubUser());
    }

    private User getStubUser() {
        User user = new User();
        user.setUsername("imrenagi");
        user.setPassword("imrenagi");
        user.setFirstName("imre");
        user.setLastName("nagi");
        user.setEmail("imre.nagi2812@gmail.com");
        user.setAddress("1500 Villa Street");
        user.setPhoneNumber("+12314123");
        return user;
    }


}