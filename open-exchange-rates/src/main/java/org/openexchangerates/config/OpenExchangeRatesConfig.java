package org.openexchangerates.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenExchangeRatesConfig {

    @Value("${openexchangerates.app.id}")
    private String appId;

    public String getAppId() {
        return appId;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
