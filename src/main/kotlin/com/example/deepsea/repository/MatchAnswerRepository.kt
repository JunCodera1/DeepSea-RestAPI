package com.example.deepsea.repository

import com.example.deepsea.entity.MatchAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchAnswerRepository : JpaRepository<MatchAnswer, Long> {
    // Đếm số câu trả lời của một người chơi trong một trận đấu
    @Query("SELECT COUNT(ma) FROM MatchAnswer ma WHERE ma.matchId = :matchId AND ma.userId = :userId")
    fun countByMatchIdAndUserId(matchId: Long, userId: Long): Long

    // Đếm số câu trả lời đúng của một người chơi trong một trận đấu
    @Query("SELECT COUNT(ma) FROM MatchAnswer ma WHERE ma.matchId = :matchId AND ma.userId = :userId AND ma.isCorrect = true")
    fun countByMatchIdAndUserIdAndIsCorrect(matchId: Long, userId: Long, isCorrect: Boolean): Int
}