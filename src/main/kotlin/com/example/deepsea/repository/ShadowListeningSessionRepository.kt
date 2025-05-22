package com.example.deepsea.repository

import com.example.deepsea.entity.ShadowListeningSession
import org.springframework.data.jpa.repository.JpaRepository

interface ShadowListeningSessionRepository : JpaRepository<ShadowListeningSession, String>