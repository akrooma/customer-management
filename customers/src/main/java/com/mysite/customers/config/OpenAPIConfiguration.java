package com.mysite.customers.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        var server = new Server()
                .url("http://localhost:8080")
                .description("Development");

        var myContact = new Contact()
                .name("API support")
                .email("support@mysite.com");

        var information = new Info()
                .title("Customer Management API")
                .version("1.0")
                .description("This API exposes endpoints to manage customers.")
                .contact(myContact);

        /*
         * It seems some packages (springfox?) enable filtering endpoints based on a path regex.
         * It could prove to be useful for filtering only for v1 endpoints.
         */

        return new OpenAPI()
                .info(information)
                .servers(List.of(server));
    }
}
