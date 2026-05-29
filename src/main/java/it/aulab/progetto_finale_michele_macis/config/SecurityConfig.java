package it.aulab.progetto_finale_michele_macis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import it.aulab.progetto_finale_michele_macis.services.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        https.csrf(crsft -> crsft.disable())
            .authorizeHttpRequests((authorize) ->
            authorize.requestMatchers("/register/**").permitAll()
            .requestMatchers("/admin/dashboard", "/categories/create", "/categories/edit/{id}", "/categories/update/{id}").hasRole("ADMIN")
            .requestMatchers("/revisor/dashboard", "revisor/detail/{id}", "/accept").hasRole("REVISOR")
            .requestMatchers("/writer/dashboard", "/article/create", "/articles/edit/{id}", "/articles/update/{id}", "/articles/delete/{id}").hasRole("WRITER")
            .requestMatchers("/register", "/", "/articles", "/images/**", "/articles/detail/**", "/categories/search/{id}", "/search/{id}", "/articles/search").permitAll()
            .anyRequest().authenticated()
        ).formLogin(form ->
            form.loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .permitAll()
        ).logout(logout ->logout.logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
            .permitAll()
        ).exceptionHandling(exception -> exception.accessDeniedPage("/error/403"))
            .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .maximumSessions(1)
        .expiredUrl("/login?session-expired=true")
        );

        return https.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
    throws Exception {
        auth.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
    throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}