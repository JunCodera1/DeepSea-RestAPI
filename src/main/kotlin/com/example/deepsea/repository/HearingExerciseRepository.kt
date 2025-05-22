package com.example.deepsea.repository

import com.example.deepsea.entity.HearingExercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface HearingExerciseRepository : JpaRepository<HearingExercise, String> {
    @Query("SELECT e FROM HearingExercise e WHERE e.unitId = :unitId ORDER BY RANDOM() LIMIT 1")
    fun findRandomByUnitId(unitId: Long): HearingExercise?
}