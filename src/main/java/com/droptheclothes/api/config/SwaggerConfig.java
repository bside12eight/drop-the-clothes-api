package com.droptheclothes.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@OpenAPIDefinition(info = @Info(title="DroptheClothes API") )
public class SwaggerConfig {

//    aaa
    private final String SWAGGER_UI_SERVER_PROPERTY_KEY = "springdoc.swagger-ui.server";

    @Autowired
    private Environment environment;

    @Bean
    @Profile({"dev"})
    public OpenAPI openAPI() {
        String server = environment.getProperty(SWAGGER_UI_SERVER_PROPERTY_KEY);
        return new OpenAPI().servers(List.of(new Server().url(server)));
    }
}
