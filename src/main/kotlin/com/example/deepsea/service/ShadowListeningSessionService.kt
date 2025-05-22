package com.example.deepsea.service

import com.example.deepsea.entity.ShadowListeningSession
import com.example.deepsea.repository.ShadowListeningSessionRepository
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class ShadowListeningSessionService(private val repository: ShadowListeningSessionRepository) {

    fun getAllSessions(): List<ShadowListeningSession> {
        return repository.findAll()
    }

    fun getSessionById(id: String): ShadowListeningSession {
        return repository.findById(id)
            .orElseThrow { NoSuchElementException("Session with id $id not found") }
    }

    fun createSession(session: ShadowListeningSession): ShadowListeningSession {
        return repository.save(session)
    }

    fun updateSession(id: String, updatedSession: ShadowListeningSession): ShadowListeningSession {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("Session with id $id not found")
        }
        val sessionToUpdate = updatedSession.copy(id = id)
        return repository.save(sessionToUpdate)
    }

    fun deleteSession(id: String) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("Session with id $id not found")
        }
        repository.deleteById(id)
    }
}