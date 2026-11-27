package com.substring.auth.auth_app_backend.config;

import com.substring.auth.auth_app_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private  JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                        .cors(Customizer.withDefaults())
                        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(authorizeHttpRequests ->
                              authorizeHttpRequests
                                    .requestMatchers("/api/v1/auth").permitAll()
                                    .requestMatchers("/api/v1/auth/login").permitAll()
                                    .anyRequest().authenticated()
        )
             //   .httpBasic(Customizer.withDefaults());

                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, e) -> {
                    //error message
                    e.printStackTrace();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    String message = "unauthorized access! "+ e.getMessage();
                    Map<String, String> errorMap=Map.of("message", message,  "statusCode", Integer.toString(401));
                    var objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(errorMap));
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//
//    @Bean
//    public UserDetailsService users() {
//
//        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//
//    UserDetails user1 = userBuilder.username("abc").password("abc").roles("ADMIN").build();
//    UserDetails user2 = userBuilder.username("abcc").password("abcc").roles("ADMIN").build();
//
//    return new InMemoryUserDetailsManager(user1, user2);
//    }



}
