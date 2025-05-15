package com.example.deepsea.repository

import com.example.deepsea.model.User
import com.example.deepsea.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*


interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    override fun findById(id: Long): Optional<UserProfile> {
        TODO("Not yet implemented")
    }
    fun findByUserUsername(username: String): UserProfile?
    fun findByUserId(userId: Long): UserProfile?

    fun findAllByOrderByTotalXpDesc(): List<UserProfile>

    fun findTop10ByOrderByTotalXpDesc(): List<UserProfile>

    @Query("SELECT COUNT(u) + 1 FROM UserProfile u WHERE u.totalXp > :xp")
    fun findUserRankByXp(xp: Int): Int


}