package com.example.deepsea.service

import com.example.deepsea.dto.UnitGuideDto
import com.example.deepsea.repository.KeyPhraseRepository
import com.example.deepsea.repository.TipRepository
import com.example.deepsea.repository.UnitRepository
import org.springframework.stereotype.Service

@Service
class UnitGuideService(
    private val unitRepository: UnitRepository,
    private val keyPhraseRepository: KeyPhraseRepository,
    private val tipRepository: TipRepository
) {
    fun getUnitGuide(unitId: Long): UnitGuideDto {
        val unit = unitRepository.findById(unitId).orElseThrow {
            RuntimeException("Unit not found with id: $unitId")
        }

        val keyPhrases = keyPhraseRepository.findByUnitIdOrderByOrderIndex(unitId)
        val tips = tipRepository.findByUnitIdOrderByOrderIndex(unitId)

        return UnitGuideDto(
            unitId = unitId,
            title = "${unit.title} Guidebook",
            description = "Explore grammar tips and key phrases for this unit",
            tips = tips.map { it.toDto() },
            keyPhrases = keyPhrases.map { it.toDto() }
        )
    }
}