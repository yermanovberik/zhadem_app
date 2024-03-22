package com.app.zhardem.configurations.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paypal")
public record PaypalProperties(

      String clientId,

      String clientSecret,

     String mode
) {
}
