package com.imrenagi.service_account.service.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

/**
 * Extended implementation of {@link org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices}
 * <p>
 * By default, it designed to return only user details. This class provides {@link #getRequest(Map)} method, which
 * returns clientId and scope of calling service. This information used in controller's security checks.
 */

public class CustomUserInfoTokenServices implements ResourceServerTokenServices {
    private static final String[] PRINCIPAL_KEYS = new String[]{"user", "username", "userid", "user_id", "login", "id", "name"};
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final String userInfoEndpointUrl;
    private final String clientId;
    private OAuth2RestOperations restTemplate;
    private String tokenType = "Bearer";
    private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

    public CustomUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        this.authoritiesExtractor = authoritiesExtractor;
    }

    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map map = this.getMap(this.userInfoEndpointUrl, accessToken);
        if (map.containsKey("error")) {
            this.logger.debug("userinfo returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        } else {
            return this.extractAuthentication(map);
        }
    }

    private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        Object principal = this.getPrincipal(map);
        List<GrantedAuthority> authorities = getAuthorities(map);

        OAuth2Request request = new OAuth2Request((Map) null, this.clientId, authorities, true, (Set) null, (Set) null, (String) null, (Set) null, (Map) null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }

    private List<GrantedAuthority> getAuthorities(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> authorityList = (List<Map<String, String>>) map.get("authorities");

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Map<String, String> authority : authorityList) {
            authorities.add(new SimpleGrantedAuthority(authority.get("authority")));
        }
        return authorities;
    }

    private Object getPrincipal(Map<String, Object> map) {
        String[] var2 = PRINCIPAL_KEYS;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String key = var2[var4];
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }

        return "unknown";
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private Map<String, Object> getMap(String path, String accessToken) {
        this.logger.info("Getting user info from: " + path);

        try {
            Object ex = this.restTemplate;
            if (ex == null) {
                BaseOAuth2ProtectedResourceDetails existingToken = new BaseOAuth2ProtectedResourceDetails();
                existingToken.setClientId(this.clientId);
                ex = new OAuth2RestTemplate(existingToken);
            }

            OAuth2AccessToken existingToken1 = ((OAuth2RestOperations) ex).getOAuth2ClientContext().getAccessToken();
            if (existingToken1 == null || !accessToken.equals(existingToken1.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                token.setTokenType(this.tokenType);
                ((OAuth2RestOperations) ex).getOAuth2ClientContext().setAccessToken(token);
            }

            return (Map) ((OAuth2RestOperations) ex).getForEntity(path, Map.class, new Object[0]).getBody();
        } catch (Exception var6) {
            this.logger.info("Could not fetch user details: " + var6.getClass() + ", " + var6.getMessage());
            return Collections.singletonMap("error", "Could not fetch user details");
        }
    }
}
