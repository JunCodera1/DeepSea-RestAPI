package com.example.deepsea.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UnitRepository : JpaRepository<com.example.deepsea.entity.Unit, Long> {
    fun findBySectionIdOrderByOrderIndex(sectionId: Long): List<com.example.deepsea.entity.Unit>

    fun findFirstBySectionIdAndOrderIndexGreaterThanOrderByOrderIndex(sectionId: Long, orderIndex: Int): Unit?

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.sectionId = :sectionId")
    fun countBySectionId(sectionId: Long): Int

    @Query("SELECT SUM(l.starsReward) FROM Lesson l WHERE l.unitId IN " +
            "(SELECT u.id FROM Unit u WHERE u.sectionId = :sectionId)")
    fun getTotalStarsBySectionId(sectionId: Long): Int?
}