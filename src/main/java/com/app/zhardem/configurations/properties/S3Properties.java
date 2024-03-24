package com.app.zhardem.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record S3Properties(
     String accessKey,

     String secretKey
) {
}
