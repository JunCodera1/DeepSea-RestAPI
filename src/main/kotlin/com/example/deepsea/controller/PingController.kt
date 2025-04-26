package com.example.deepsea.controller
import com.example.deepsea.dto.SurveySelectionDto
import com.example.deepsea.model.UserProfile
import com.example.deepsea.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PingController() {
    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> = ResponseEntity.ok("pong")

}