package com.example.deepsea.repository

import com.example.deepsea.entity.Lesson
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : JpaRepository<Lesson, Long> {
    fun findByUnitIdOrderByOrderIndex(unitId: Long): List<Lesson>

    fun findFirstByUnitIdAndOrderIndexGreaterThanOrderByOrderIndex(unitId: Long, orderIndex: Int): Lesson?

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.unitId = :unitId")
    fun countByUnitId(unitId: Long): Int

    @Query("SELECT SUM(l.starsReward) FROM Lesson l WHERE l.unitId = :unitId")
    fun getTotalStarsByUnitId(unitId: Long): Int
}