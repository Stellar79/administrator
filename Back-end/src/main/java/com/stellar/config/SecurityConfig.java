package com.stellar.config;

import com.stellar.security.CaptchaFilter;
import com.stellar.security.JwtAccessDeniedHandler;
import com.stellar.security.JwtAuthenticationEntryPoint;
import com.stellar.security.JwtAuthenticationFilter;
import com.stellar.security.JwtLogoutSuccessHandler;
import com.stellar.security.LoginFailureHandler;
import com.stellar.security.LoginSuccessHandler;
import com.stellar.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    public static final String[] URL_WHITELIST = {
//        "/webjars/**",
//        "/test/**",
        "/favicon.ico",
        "/captcha",
        "/login",
        "/logout",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable()
            // Login config
            .formLogin()
            .failureHandler(loginFailureHandler)
            .successHandler(loginSuccessHandler)
            // Logout
            .and()
            .logout().logoutSuccessHandler(jwtLogoutSuccessHandler)


            //
            .and()
            .authorizeRequests()
            .antMatchers(URL_WHITELIST).permitAll() // 白名单
            .anyRequest().authenticated()

            // session
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // Exception
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            // filter
            .and()
            .addFilter(jwtAuthenticationFilter())
            .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    };

}
