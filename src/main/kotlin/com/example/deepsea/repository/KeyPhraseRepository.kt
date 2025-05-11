package com.example.deepsea.repository

import com.example.deepsea.model.KeyPhrase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KeyPhraseRepository : JpaRepository<KeyPhrase, Long> {
    fun findByUnitIdOrderByOrderIndex(unitId: Long): List<KeyPhrase>
}
