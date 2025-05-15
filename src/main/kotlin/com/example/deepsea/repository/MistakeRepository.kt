package com.example.deepsea.repository

import com.example.deepsea.model.Mistake
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MistakeRepository : JpaRepository<Mistake, Long> {
    fun findByUserId(userId: Long): List<Mistake>
    fun findByUserIdAndWord(userId: Long, word: String): List<Mistake>
    fun countByUserId(userId: Long): Long
}