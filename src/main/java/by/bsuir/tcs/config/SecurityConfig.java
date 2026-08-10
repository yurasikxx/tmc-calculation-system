package by.bsuir.tcs.config;

import by.bsuir.tcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/departments/**", "/professions/**").hasAnyRole("LABOR", "ADMIN")
                        .requestMatchers("/employees/**").hasAnyRole("LABOR", "ADMIN", "MTS")
                        .requestMatchers("/tmc-items/siz/**").hasRole("OT")
                        .requestMatchers("/tmc-items/tool/**").hasRole("STOREKEEPER")
                        .requestMatchers("/tmc-items/equipment/**").hasRole("TECHNOLOG")
                        .requestMatchers("/norms/siz/**").hasRole("OT")
                        .requestMatchers("/norms/tool/**").hasRole("STOREKEEPER")
                        .requestMatchers("/norms/equipment/**").hasRole("TECHNOLOG")
                        .requestMatchers("/calculations/**", "/api/**").hasRole("MTS")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}