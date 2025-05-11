package com.example.deepsea.controller

import com.example.deepsea.dto.UnitDto
import com.example.deepsea.dto.UnitGuideDto
import com.example.deepsea.dto.UnitProgressDto
import com.example.deepsea.service.UnitGuideService
import com.example.deepsea.service.UnitService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/units")
class UnitController(private val unitService: UnitService, private val unitGuideService: UnitGuideService) {

    @GetMapping("/section/{sectionId}")
    fun getUnitsBySection(@PathVariable sectionId: Long): ResponseEntity<List<UnitDto>> {
        return ResponseEntity.ok(unitService.getUnitsBySection(sectionId))
    }

    @GetMapping("/{id}")
    fun getUnitById(@PathVariable id: Long): ResponseEntity<UnitDto> {
        return ResponseEntity.ok(unitService.getUnitById(id))
    }

    @GetMapping("/{id}/progress")
    fun getUnitProgress(@PathVariable id: Long, @RequestParam userId: Long): ResponseEntity<UnitProgressDto> {
        return ResponseEntity.ok(unitService.getUnitProgress(id, userId))
    }

    @GetMapping("/{unitId}/guide")
    fun getUnitGuide(@PathVariable unitId: Long): ResponseEntity<UnitGuideDto> {
        return ResponseEntity.ok(unitGuideService.getUnitGuide(unitId))
    }
}