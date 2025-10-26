package com.moger.crudproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //user table - password
    @Value("${userPassword}")
    public String user_password;

/*    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/students").hasRole("STU")
                        .requestMatchers(HttpMethod.GET, "/students/**").hasRole("STU")
                        .requestMatchers(HttpMethod.POST, "/students/**").hasRole("STU")
                        .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("STU")
                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")
                        .requestMatchers("/v3/api-docs/**",  // OpenAPI documentation
                                "/swagger-ui/**",   // Swagger UI resources
                                "/swagger-ui.html"  // Swagger UI HTML page
                        ).permitAll()
                        .anyRequest().authenticated()
        );
        //Using basic authentication
        http.httpBasic((Customizer.withDefaults()));

        //disable csrf-cross site request forgery
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
*/

//h2 database - Integration testing - to permit all requests
  @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/students").permitAll()
                                .requestMatchers("/students/**").permitAll()

                                //.requestMatchers("/h2-console/**").permitAll()

                                .anyRequest().authenticated()

                ).build();
    }

//hardcoded user details


/*  @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);  //all the user details stored in the database
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails pansy = User.builder()
                .username("pansy")
                .password("{noop}test")
                .roles("STUDENT")
                .build();

        UserDetails lily = User.builder()
                .username("lily")
                .password("{noop}test")
                .roles("STUDENT")
                .build();
        return new InMemoryUserDetailsManager(pansy, lily);
    } */
}
