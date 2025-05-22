package com.example.deepsea.repository

import com.example.deepsea.entity.UserProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserProgressRepository : JpaRepository<UserProgress, Long> {
    fun findByUserIdAndLessonId(userId: Long, lessonId: Long): UserProgress?

    fun findByUserIdAndUnitId(userId: Long, unitId: Long): List<UserProgress>

    @Query("SELECT up FROM UserProgress up WHERE up.userId = :userId AND up.unitId IN " +
            "(SELECT u.id FROM Unit u WHERE u.sectionId = :sectionId)")
    fun findByUserIdAndSectionId(userId: Long, sectionId: Long): List<UserProgress>

    @Query("SELECT COUNT(DISTINCT up.lessonId) FROM UserProgress up WHERE up.userId = :userId " +
            "AND up.unitId = :unitId AND up.completed = true")
    fun countCompletedLessonsByUserIdAndUnitId(userId: Long, unitId: Long): Int

    @Query("SELECT SUM(up.starsEarned) FROM UserProgress up WHERE up.userId = :userId " +
            "AND up.unitId = :unitId AND up.completed = true")
    fun sumStarsEarnedByUserIdAndUnitId(userId: Long, unitId: Long): Int?

    @Query("SELECT COUNT(DISTINCT up.unitId) FROM UserProgress up WHERE up.userId = :userId " +
            "AND up.unitId IN (SELECT u.id FROM Unit u WHERE u.sectionId = :sectionId) " +
            "AND (SELECT COUNT(up2) FROM UserProgress up2 WHERE up2.userId = :userId AND up2.unitId = up.unitId AND up2.completed = true) = " +
            "(SELECT COUNT(l) FROM Lesson l WHERE l.unitId = up.unitId)")
    fun countCompletedUnitsByUserIdAndSectionId(userId: Long, sectionId: Long): Int

    @Query("SELECT SUM(up.starsEarned) FROM UserProgress up WHERE up.userId = :userId " +
            "AND up.unitId IN (SELECT u.id FROM Unit u WHERE u.sectionId = :sectionId) " +
            "AND up.completed = true")
    fun sumStarsEarnedByUserIdAndSectionId(userId: Long, sectionId: Long): Int?

    @Query("SELECT DISTINCT up.unitId FROM UserProgress up WHERE up.userId = :userId " +
            "AND up.completed = true ORDER BY up.completionDate DESC")
    fun findRecentCompletedUnitsByUserId(userId: Long, limit: Int): List<Long>

    @Query("SELECT up FROM UserProgress up WHERE up.userId = :userId AND up.completed = true " +
            "ORDER BY up.completionDate DESC")
    fun findMostRecentCompletedLessonByUserId(userId: Long): UserProgress?
}