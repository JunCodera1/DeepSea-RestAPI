package com.example.deepsea.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AdminDashboardWebConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        // Forward tất cả các path con của /admin-dashboard trừ các file tĩnh
        registry.addViewController("/admin-dashboard/{spring:[\\w\\-]+}")
            .setViewName("forward:/admin-dashboard/dist/index.html")
        registry.addViewController("/admin-dashboard/{spring:[\\w\\-]+}/{spring2:[\\w\\-]+}")
            .setViewName("forward:/admin-dashboard/dist/index.html")
    }
}
