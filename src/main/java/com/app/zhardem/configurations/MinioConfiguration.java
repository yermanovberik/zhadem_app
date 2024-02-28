package com.app.zhardem.configurations;

import io.minio.MinioClient;
import io.minio.MinioProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.endpoint())
                .credentials(
                        minioProperties.accessKey(),
                        minioProperties.secretKey()
                )
                .build();
    }

}*/
