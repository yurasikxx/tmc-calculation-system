package by.bsuir.tcs.config;

import by.bsuir.tcs.config.security.CustomAuthenticationFailureHandler;
import by.bsuir.tcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/login", "/error",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/departments/**", "/professions/**")
                        .hasAnyRole("LABOR", "ADMIN")
                        .requestMatchers("/employees/**")
                        .hasAnyRole("LABOR", "MTS", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tmc-items/**")
                        .hasAnyRole("OT", "TECHNOLOG", "STOREKEEPER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/tmc-items/create",
                                "/tmc-items/edit/**",
                                "/tmc-items/delete/**")
                        .hasAnyRole("OT", "TECHNOLOG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/norms/**")
                        .hasAnyRole("OT", "TECHNOLOG", "STOREKEEPER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/norms/create",
                                "/norms/edit/**",
                                "/norms/delete/**")
                        .hasAnyRole("OT", "TECHNOLOG", "ADMIN")
                        .requestMatchers("/issued-items/**")
                        .hasAnyRole("STOREKEEPER", "ADMIN")
                        .requestMatchers("/staffing-plans/**", "/calculations/**", "/forecast/**")
                        .hasAnyRole("MTS", "ADMIN")
                        .requestMatchers("/api/reports/**")
                        .hasAnyRole("MTS", "ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .userDetailsService(userService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}