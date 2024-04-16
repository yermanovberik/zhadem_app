package com.app.zhardem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import tech.ailef.snapadmin.external.SnapAdminAutoConfiguration;


@SpringBootApplication
@ConfigurationPropertiesScan(value = "com.app.zhardem.configurations.properties")
@ImportAutoConfiguration(SnapAdminAutoConfiguration.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
