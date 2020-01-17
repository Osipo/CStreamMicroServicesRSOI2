package ru.osipov.deploy.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.osipov.deploy.config.jwt.JwtConfigurer;
import ru.osipov.deploy.services.jwt.JwtTokenProvider;
import ru.osipov.deploy.services.jwt.JwtUserDetailsService;
import ru.osipov.deploy.config.jwt.JwtAuthenticationEntryPoint;
import ru.osipov.deploy.config.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtUserDetailsService customUserDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/v1/api/admin/**";
    private static final String OAUTH_ENDPOINT = "/v1/api/oauth/**";
    private static final String REGISTRATION_ENDPOINT = "/v1/api/registration";
    private static final String USERS_BY_ID_ENDPOINT = "/v1/api/users/{id}";
    private static final String CLIENTS_BY_ID_ENDPOINT = "/v1/api/clients/{id}";
    private static final String REG_USER_ENDPOINT = "/v1/api/users/reg";
    private static final String REG_CLIENT_ENDPOINT = "/v1/api/clients/reg";
    private static final String SIGN_IN_ENDPOINT = "/v1/api/sign_in";
    private static final String CSS_ENDPOINT = "/css/**";
    private static final String JS_ENDPOINT = "/js/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider provider){
        this.jwtTokenProvider = provider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(jwtTokenProvider.passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                .antMatchers(OAUTH_ENDPOINT, REGISTRATION_ENDPOINT, CSS_ENDPOINT, JS_ENDPOINT, SIGN_IN_ENDPOINT,
                        REG_CLIENT_ENDPOINT, REG_USER_ENDPOINT, USERS_BY_ID_ENDPOINT, CLIENTS_BY_ID_ENDPOINT).permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                        .permitAll()
                   .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                    .anyRequest()
                        .authenticated()
                    .and()
                    .apply(new JwtConfigurer(jwtTokenProvider))
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint());

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}