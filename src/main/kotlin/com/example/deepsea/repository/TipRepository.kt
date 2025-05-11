package com.example.deepsea.repository

import com.example.deepsea.model.Tip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TipRepository : JpaRepository<Tip, Long> {
    fun findByUnitIdOrderByOrderIndex(unitId: Long): List<Tip>
}