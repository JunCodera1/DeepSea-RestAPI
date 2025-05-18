package com.example.deepsea.repository

import com.example.deepsea.model.ShadowListeningSession
import org.springframework.data.jpa.repository.JpaRepository

interface ShadowListeningSessionRepository : JpaRepository<ShadowListeningSession, String>