package com.pryabykh.bankapp.front.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class FeignClientSecurityInterceptor {


    @Bean
    public RequestInterceptor getRequestInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        return requestTemplate -> {
            OAuth2AuthorizedClient client = oAuth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
                    .withClientRegistrationId("bankapp")
                    .principal("system")
                    .build()
            );
            String accessToken = client.getAccessToken().getTokenValue();
            requestTemplate
                    .header("Authorization", "Bearer " + accessToken);
        };
    }
}
