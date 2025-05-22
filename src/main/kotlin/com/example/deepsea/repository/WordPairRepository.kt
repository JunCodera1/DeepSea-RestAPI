package com.example.deepsea.repository

import com.example.deepsea.entity.WordPair
import org.springframework.data.jpa.repository.JpaRepository

interface WordPairRepository : JpaRepository<WordPair, Long> {
    fun findByUnitId(unitId: Long): List<WordPair>
}