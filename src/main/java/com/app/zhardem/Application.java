package com.app.zhardem;

import com.app.zhardem.configurations.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@ConfigurationPropertiesScan(value = "com.app.zhardem.configurations.properties")
@EnableConfigurationProperties(MinioProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
