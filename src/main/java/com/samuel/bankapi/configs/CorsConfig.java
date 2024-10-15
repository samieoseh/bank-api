package com.samuel.bankapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Adjust the path as needed
                .allowedOrigins("http://localhost:8081", "exp://192.168.1.45:8081", "http://localhost:5173", "https://bank-website-git-theming-samieosehs-projects.vercel.app") // Add your allowed origins
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders(
                       "Authorization",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Headers",
                        "Access-Control-Allow-Methods",
                        "Content-Type",
                        "Origin",
                        "Accept",
                        "X-Requested-With",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
                .maxAge(3600)
                .allowCredentials(true);
    }
}
