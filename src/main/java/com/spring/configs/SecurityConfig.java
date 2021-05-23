package com.spring.configs;


import com.spring.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Класс конфигурации Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Служба с дополнительной информацией о пользователе для Spring Security.
     */
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Бин с созданием шифровщика паролей на основе алгоритма bcrypt.
     * @return Шифровщик паролей на основе алгоритма bcrypt
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Задание конфигурации для Spring Security.
     * @param http класс конфигурации для Spring Security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/webjars/**",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/registration",
                        "/",
                        "/products",
                        "/product/{vendorCode:\\d+}",
                        "/product/imageDisplay**",
                        "/search**"
                ).permitAll()
                .antMatchers("/product/add", "/product/update", "/product/delete", "/cart**", "/api**").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400)
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .deleteCookies("remember-me")
                .permitAll();
    }

    /**
     * Бин с созданием менеджера аутентификации.
     * @return менеджер аутентификации
     */
    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    /**
     * Задание менеджера аутентификации.
     * @param auth сборщик менеджера аутентификации
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}