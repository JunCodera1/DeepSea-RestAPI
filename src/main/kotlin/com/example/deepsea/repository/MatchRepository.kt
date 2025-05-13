package com.example.deepsea.repository

import com.example.deepsea.model.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository : JpaRepository<Match, Long> {
    // Tìm các trận đấu đang diễn ra của một người chơi
    fun findByPlayer1IdAndStatus(player1Id: Long, status: String): List<Match>

    // Tìm các trận đấu đã hoàn thành của một người chơi
    fun findByPlayer1IdOrPlayer2IdAndStatus(player1Id: Long, player2Id: Long, status: String): List<Match>
}