package com.example.deepsea.repository

import com.example.deepsea.model.Word
import org.springframework.data.jpa.repository.JpaRepository

interface WordRepository : JpaRepository<Word, String> {
    fun findByTheme(theme: String): List<Word>
}