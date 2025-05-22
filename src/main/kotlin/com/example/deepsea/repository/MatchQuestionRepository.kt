package com.example.deepsea.repository

import com.example.deepsea.entity.MatchQuestion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MatchQuestionRepository : JpaRepository<MatchQuestion, Long> {
    // Lấy danh sách câu hỏi của một trận đấu theo thứ tự
    @Query("SELECT mq FROM MatchQuestion mq WHERE mq.matchId = :matchId ORDER BY mq.questionOrder")
    fun findByMatchId(matchId: Long): List<MatchQuestion>

    // Đếm số câu hỏi trong một trận đấu
    @Query("SELECT COUNT(mq) FROM MatchQuestion mq WHERE mq.matchId = :matchId")
    fun countByMatchId(matchId: Long): Long
}