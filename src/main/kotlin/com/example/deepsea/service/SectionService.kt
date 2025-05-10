package com.example.deepsea.service

import com.example.deepsea.dto.SectionDto
import com.example.deepsea.model.toDto
import com.example.deepsea.repository.SectionRepository
import com.example.deepsea.repository.UnitRepository
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository,
    private val unitRepository: UnitRepository
) {

    fun getAllSections(): List<SectionDto> {
        return sectionRepository.findAllByOrderByOrderIndex().map { section ->
            val unitCount = unitRepository.countBySectionId(section.id)
            section.toDto(unitCount = unitCount)
        }
    }

    fun getSectionById(id: Long): SectionDto {
        val section = sectionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Section not found") }
        val unitCount = unitRepository.countBySectionId(section.id)
        return section.toDto(unitCount = unitCount)
    }

    fun getSectionsByLevel(level: String): List<SectionDto> {
        return sectionRepository.findByLevel(level).map { section ->
            val unitCount = unitRepository.countBySectionId(section.id)
            section.toDto(unitCount = unitCount)
        }
    }
}