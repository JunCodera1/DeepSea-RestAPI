package com.example.deepsea.repository

import com.example.deepsea.entity.Leaderboard
import org.springframework.data.jpa.repository.JpaRepository
import org

.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface LeaderboardRepository : JpaRepository<Leaderboard, Long> {
    // Lấy bảng xếp hạng theo tuần, sắp xếp theo điểm số giảm dần
    @Query("SELECT l FROM Leaderboard l WHERE l.weekStartDate = :weekStartDate ORDER BY l.score DESC")
    fun findByWeekStartDateOrderByScoreDesc(weekStartDate: LocalDate): List<Leaderboard>

    // Lấy bảng xếp hạng theo tuần, sắp xếp theo thứ hạng tăng dần
    @Query("SELECT l FROM Leaderboard l WHERE l.weekStartDate = :weekStartDate ORDER BY l.rank ASC")
    fun findByWeekStartDateOrderByRankAsc(weekStartDate: LocalDate): List<Leaderboard>

    // Tìm mục leaderboard của một người chơi trong tuần
    fun findByUserIdAndWeekStartDate(userId: Long, weekStartDate: LocalDate): Leaderboard?
}