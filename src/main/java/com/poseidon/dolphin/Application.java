package com.poseidon.dolphin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
@EnableJpaAuditing
@EnableOAuth2Client
@EnableCaching
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
	
	@Bean
	public ResourceBundleMessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages");
	    return messageSource;
	}
	
}

