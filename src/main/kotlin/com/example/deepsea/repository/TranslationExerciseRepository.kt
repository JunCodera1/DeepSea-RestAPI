package com.example.deepsea.repository

import com.example.deepsea.model.TranslationExercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TranslationExerciseRepository : JpaRepository<TranslationExercise, String> {
    @Query("SELECT e FROM TranslationExercise e ORDER BY RANDOM() LIMIT 1")
    fun findRandomExercise(): TranslationExercise?
}