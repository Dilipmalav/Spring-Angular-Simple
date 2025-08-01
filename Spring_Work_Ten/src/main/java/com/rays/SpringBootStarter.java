package com.rays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringBootStarter {

	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootStarter.class, args);
		
	}
		@Bean
		public WebMvcConfigurer corsConfigurer() {

			WebMvcConfigurer w = new WebMvcConfigurer() {
				
				public void addCorsMappings(CorsRegistry registry) {
					CorsRegistration cors = registry.addMapping("/**");
					cors.allowedOrigins("http://localhost:4200");
			}
			
		};
			return w;
	}
}
