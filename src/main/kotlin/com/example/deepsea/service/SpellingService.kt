package com.example.deepsea.service

import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Service
class SpellingService(private val resourceLoader: ResourceLoader) {

    // Cache for character audio files
    private val characterAudioCache = ConcurrentHashMap<String, Resource>()

    /**
     * Get audio resource for a specific character
     */
    fun getCharacterAudio(character: String): Resource? {
        return try {
            characterAudioCache.computeIfAbsent(character) {
                // Đường dẫn đến file audio cho từng ký tự
                resourceLoader.getResource("classpath:audio/characters/${character}.mp3")
            }
        } catch (e: IOException) {
            null
        }
    }

    /**
     * Get phonetic breakdown of a word
     */
    fun getPhoneticBreakdown(word: String): List<String> {
        return when (word) {
            "ください" -> listOf("ku", "da", "sa", "i")
            "おちゃ" -> listOf("o", "cha")
            "ごはん" -> listOf("go", "ha", "n")
            else -> word.map { it.toString() }
        }
    }

    /**
     * Get all audio files for spelling out a word
     */
    fun getSpellingAudioFiles(word: String): List<Resource> {
        val phoneticParts = getPhoneticBreakdown(word)
        return phoneticParts.mapNotNull { getCharacterAudio(it) }
    }
}