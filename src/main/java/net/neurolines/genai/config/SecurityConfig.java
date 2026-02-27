package net.neurolines.genai.config;

import net.neurolines.genai.handler.AuthenticationFailureHandler;
import net.neurolines.genai.handler.AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationSuccessHandler getSuccessHandler() {
        return new AuthenticationSuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

/*        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->auth.anyRequest().permitAll());*/


        //  .logoutUrl("/auth/logout")
        //      .logoutSuccessUrl("/member/login")
        http

                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(org.springframework.web.cors.CorsUtils::isPreFlightRequest).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                      //  .loginPage("/member/login") // 로그인 페이지 경로 (확장자 없음)
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(getSuccessHandler())
                        //.defaultSuccessUrl("/pages/msds-register")
                        .failureHandler(new AuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
/*                .sessionManagement(session -> session
                        .invalidSessionUrl("/member/login") // 세션 만료 시 이동할 URL
                )*/
                .headers(header -> header
                        .frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        ))


        ;


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8011")); // 명확한 Origin 지정
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
