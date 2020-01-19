package ru.osipov.deploy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.osipov.deploy.errors.CustomAccessDeniedHandler;
import ru.osipov.deploy.jwtconf.JwtAuthenticationEntryPoint;
import ru.osipov.deploy.jwtconf.JwtConfigurer;
import ru.osipov.deploy.services.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

//    private static final String AUTH_ENDPOINT = "/v1/api/auth/**";
//    private static final String ALL_ENDPOINT = "/v1/api/**";
//    private static final String ADMIN_ENDPOINT = "/v1/api/admin/**";
//    private static final String OAUTH_ENDPOINT = "/v1/api/oauth/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider provider) {
        this.jwtTokenProvider = provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PATCH,"/v1/api/films/{id: [\\d+]}").authenticated()
                .antMatchers(HttpMethod.POST,"/v1/api/genres/delete/{id: [\\d+]}").authenticated()
                .antMatchers(HttpMethod.POST, "/v1/api/genres/create").authenticated()
                .antMatchers(HttpMethod.POST, "/v1/api/films/genre/{ogid: [\\d+]}").authenticated()
                .antMatchers(HttpMethod.PATCH, "/v1/api/cinemas/{cid: [\\d+]}").authenticated()
//                .antMatchers(AUTH_ENDPOINT).authenticated()
//                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
//                .antMatchers(OAUTH_ENDPOINT).permitAll()
                .anyRequest().permitAll()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}
