package ru.osipov.deploy;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/v1/api/films/{film_id:[\\d+]}").authorizeRequests()
                .antMatchers("/", "/login**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .antMatcher("/v1/api/genres/delete/{genre_id:[\\d+]}")
                .authorizeRequests()
                .anyRequest()
                .authenticated().and().oauth2Login()
                .and().antMatcher("/v1/api/genres/create").authorizeRequests()
                .anyRequest().authenticated()
                .and().oauth2Login()
                .and()
                .antMatcher("/v1/api/films/genre/{genre_id:[\\d+]}")
                .authorizeRequests()
                .anyRequest().authenticated().and().oauth2Login().and()
                .antMatcher("/v1/api/cinemas/{cinema_id:[\\d+]}").authorizeRequests()
                .anyRequest().authenticated().and().oauth2Login();
    }
}