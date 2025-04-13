package com.example.deepsea.config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/",
                        "/index.html",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/auth/register",
                        "/auth/login"
                    ).permitAll() // Cho phép truy cập không cần đăng nhập
                    .anyRequest().authenticated() // Còn lại cần login
            }
        return http.build()
    }
}
