package com.example.deepsea.repository

import com.example.deepsea.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    override fun findById(id: Long): Optional<UserProfile> {
        TODO("Not yet implemented")
    }
    fun findByUserUsername(username: String): UserProfile?
    fun findByUserId(userId: Long): UserProfile?
}