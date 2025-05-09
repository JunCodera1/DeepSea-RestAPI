package com.example.deepsea.controller

import com.example.deepsea.dto.SpellingResponse
import com.example.deepsea.service.SpellingService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/spelling")
class SpellingController(private val spellingService: SpellingService) {

    @GetMapping("/{word}")
    fun getSpellingInfo(@PathVariable word: String): ResponseEntity<SpellingResponse> {
        val phoneticParts = spellingService.getPhoneticBreakdown(word)

        val response = SpellingResponse(
            word = word,
            phoneticParts = phoneticParts,
            audioUrls = phoneticParts.map { "/api/spelling/audio/$it" }
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/audio/{character}")
    fun getCharacterAudio(@PathVariable character: String): ResponseEntity<Resource> {
        val resource = spellingService.getCharacterAudio(character)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("audio/mpeg"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${character}.mp3\"")
            .body(resource)
    }
}
