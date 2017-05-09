package com.imrenagi.service_account.client;

import com.imrenagi.service_account.domains.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by imrenagi on 5/8/17.
 */
@FeignClient(name = "service-auth")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/uaa/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void createUser(User user);

}
