package com.app.zhardem.configurations;

import com.app.zhardem.configurations.properties.PaypalProperties;
import com.paypal.base.rest.APIContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PaypalConfig {


    @Autowired
    private PaypalProperties paypalProperties;

    @Bean
    public APIContext apiContext() {
        return new APIContext(paypalProperties.clientId(), paypalProperties.clientSecret(), paypalProperties.mode());
    }
}