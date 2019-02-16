package com.poseidon.dolphin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringDataWebSupport
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
	
}

