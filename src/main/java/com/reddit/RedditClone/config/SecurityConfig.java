package com.reddit.RedditClone.config;

import com.reddit.RedditClone.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.httpBasic();
        http.authorizeRequests()
                .antMatchers("/","/register","/saveUser").permitAll()
//                .antMatchers("").hasAnyAuthority("ADMIN", "AUTHOR")
//                .antMatchers("").hasAnyAuthority("ADMIN", "AUTHOR")
                .antMatchers("/delete/{postId}")
                .access("@userSecurity.hasUserId(authentication, #postId)")
                .antMatchers("/update/{postId}")
                .access("@userSecurity.hasUserId(authentication, #postId)")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticateUser")
                .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
        http.exceptionHandling().accessDeniedPage("/access-denied");
    }
}