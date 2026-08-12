package n1h5.models.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI openApi(){
        return new OpenAPI().info(new Info()
            .title("N1H5 Backend API")
            .version("1.0.0")
            .description("tai lieu api he thong N1H5")
            .contact(new Contact().email("hieunho500@gmail.com")))
           ;
    }
}
