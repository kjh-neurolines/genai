package net.neurolines.genai.config;

import net.neurolines.genai.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/css/**", "/font/**", "/js/**", "/script/**", "/templates/**",
                        "/images/**", "/mapper/**", "/auth/**", "/", "/error", "/.well-known/**", "/favicon.ico");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 적용
                .allowedOrigins("http://localhost:8011") // Nuxt 앱 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true); // 쿠키 전송 허용
    }

}
