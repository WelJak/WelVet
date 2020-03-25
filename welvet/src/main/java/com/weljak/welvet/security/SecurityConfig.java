package com.weljak.welvet.security;

import com.weljak.welvet.domain.owner.OwnerRepo;
import com.weljak.welvet.service.owner.OwnerServiceImpl;
import com.weljak.welvet.webapi.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OwnerRepo ownerRepo;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new OwnerServiceImpl(ownerRepo);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(Endpoints.CREATE_OWNER_ENDPOINT).permitAll()
                .antMatchers(Endpoints.CREATE_REGISTRATION_REQUEST_ENDPOINT).permitAll()
                .antMatchers(Endpoints.CONFIRM_REGISTRATION_REQUEST_ENDPOINT).permitAll()
                .antMatchers(Endpoints.OWNER_ENDPOINT + "/**").hasRole("USER")
                .antMatchers(Endpoints.ANIMAL_ENDPOINT + "/**").hasRole("USER")
                .antMatchers(Endpoints.VET_ENDPOINT + "/**").hasRole("VET")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
