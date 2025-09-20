package com.superapp.nutrilife.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NutriLife Core API")
                        .version("1.0.0")
                        .description("NutriLife uygulaması için temel mikroservis API dokümantasyonu")
                        .contact(new Contact()
                                .name("NutriLife Team")
                                .email("info@nutrilife.com")
                                .url("https://nutrilife.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
