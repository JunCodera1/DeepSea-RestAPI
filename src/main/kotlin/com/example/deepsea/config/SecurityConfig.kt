package com.example.deepsea.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/auth/**",
                        "/api/upload/**",
                        "/api/profiles/**",
                        "/api/users/**",
                        "/api/survey/**",
                        "/api/language/**",
                        "/api/ping/**",
                        "/api/password-reset/**",
                        "/api/courses/**",
                        "/api/lessons/**",
                        "/api/rank/**",
                        "/api/leaderboard/**",
                        "/api/sections/**",
                        "/api/progress/**",
                        "/api/units/**",
                        "/api/learn/**",
                        "/api/game/**",
                        "/api/language-learning/**",
                        "api/vocabulary/**",
                        "/api/exercises/**",
                        "/api/hearing-exercises/**",
                        "/api/translation-exercises/**",
                        "/api/questions/**",
                        "/api/sessions/**",
                        "/api/v2/lessons/**",
                        "/api/mistakes/**",
                        "/api/words/**",
                        "/api/stories/**",
                        "/api/payments/**",
                        "/api/sessions-listening/**",
                        "/admin-dashboard/**",
                        "/api/admin/**",
                        "/error"
                    ).permitAll()
                    .requestMatchers(
                        "/",
                        "/index.html",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/admin-dashboard/dist/**",
                        "/admin-dashboard/assets/**",
                        "/vite.svg",
                        "/favicon.ico",
                        "/error",
                        "/",
                        "/index.html",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://10.0.2.2")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}