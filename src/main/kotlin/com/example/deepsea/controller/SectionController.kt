package com.example.deepsea.controller

import com.example.deepsea.dto.SectionDto
import com.example.deepsea.service.SectionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sections")
class SectionController(private val sectionService: SectionService) {

    @GetMapping
    fun getAllSections(): ResponseEntity<List<SectionDto>> {
        return ResponseEntity.ok(sectionService.getAllSections())
    }

    @GetMapping("/{id}")
    fun getSectionById(@PathVariable id: Long): ResponseEntity<SectionDto> {
        return ResponseEntity.ok(sectionService.getSectionById(id))
    }
}