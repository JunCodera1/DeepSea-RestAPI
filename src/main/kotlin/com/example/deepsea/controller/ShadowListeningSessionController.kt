package com.example.deepsea.controller

import com.example.deepsea.entity.ShadowListeningSession
import com.example.deepsea.service.ShadowListeningSessionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sessions-listening")
class ShadowListeningSessionController(private val service: ShadowListeningSessionService) {

    @GetMapping
    fun getAllSessions(): List<ShadowListeningSession> {
        return service.getAllSessions()
    }

    @GetMapping("/{id}")
    fun getSessionById(@PathVariable id: String): ResponseEntity<ShadowListeningSession> {
        return try {
            ResponseEntity.ok(service.getSessionById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping
    fun createSession(@RequestBody session: ShadowListeningSession): ResponseEntity<ShadowListeningSession> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSession(session))
    }

    @PutMapping("/{id}")
    fun updateSession(@PathVariable id: String, @RequestBody session: ShadowListeningSession): ResponseEntity<ShadowListeningSession> {
        return try {
            ResponseEntity.ok(service.updateSession(id, session))
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteSession(@PathVariable id: String): ResponseEntity<Void> {
        return try {
            service.deleteSession(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}