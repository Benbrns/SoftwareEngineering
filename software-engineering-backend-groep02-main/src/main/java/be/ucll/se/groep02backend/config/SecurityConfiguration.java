
package be.ucll.se.groep02backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.annotation.PostConstruct;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Value("${auth.whitelist.paths}")
        private String authWhitelistPaths;

        private String[] authWhitelist;

        @PostConstruct
        private void init() {
                authWhitelist = authWhitelistPaths.split(",");
        }

        // Getter for authWhitelist
        public String[] getAuthWhitelist() {
                return authWhitelist;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf
                                                .disable())
                                .headers(headers -> headers
                                                .frameOptions(frameOptions -> frameOptions
                                                                .disable()))
                                .authorizeHttpRequests(req ->
                                req.requestMatchers(getAuthWhitelist())
                                        .permitAll()
                                        .requestMatchers("*/admin/*").hasAnyAuthority( "ADMIN")
                                        .anyRequest()
                                        .authenticated()
                        )
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();

        }
}