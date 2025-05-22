package com.example.deepsea.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class AdminDashboardController {
    @GetMapping("/admin-dashboard")
    fun adminDashboard(): String {
        return "forward:/admin-dashboard/dist/index.html"
    }
}
