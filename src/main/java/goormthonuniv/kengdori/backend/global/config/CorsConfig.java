package goormthonuniv.kengdori.backend.global.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private static final String LOCALHOST_ORIGIN = "http://localhost:5173";
    private static final String LOCAL_IP_ORIGIN = "http://10.221.82.78:5173";
    private static final String CLOUDTYPE_ORIGIN = "https://port-0-keungdori-be-mf68say742c07b64.sel5.cloudtype.app";
    private static final String VERCEL_FRONT = "https://keungdori-frontend-git-feat-login-park-junhyeongs-projects.vercel.app";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                LOCALHOST_ORIGIN,
                                LOCAL_IP_ORIGIN,
                                CLOUDTYPE_ORIGIN,
                                VERCEL_FRONT
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