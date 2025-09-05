package goormthonuniv.kengdori.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private static final String LOCALHOST_ORIGIN = "http://localhost:5173";
    private static final String LOCAL_IP_ORIGIN = "http://10.221.82.78:5173";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                LOCALHOST_ORIGIN,
                                LOCAL_IP_ORIGIN
                        )
                        .allowedMethods(
                                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
                        )
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600L);
            }
        };
    }
}