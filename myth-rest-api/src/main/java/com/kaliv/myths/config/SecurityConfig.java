//package com.kaliv.myths.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("user")
//                .password(passwordEncoder().encode("pass")).roles("USER");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.requiresChannel().anyRequest().requiresSecure();
////                .and()
////                .authorizeRequests().antMatchers("/login").permitAll()
////                .anyRequest().authenticated().and().formLogin().loginPage("/login");
//    }
//
////    @Override
////    public void configure(WebSecurity web) {
////        web.ignoring().antMatchers("/webjars/**", "/images/*", "/css/*", "/h2-console/**");
////    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
//
