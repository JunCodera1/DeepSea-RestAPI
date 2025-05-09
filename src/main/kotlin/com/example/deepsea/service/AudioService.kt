package com.example.deepsea.service

import com.example.deepsea.model.AudioResponse
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service

@Service
class AudioService(private val resourceLoader: ResourceLoader) {

    fun getAudioInfo(word: String): AudioResponse {
        // In a real implementation, you might look up this information from a database
        val fileName = when (word) {
            "ください" -> "kudasai.mp3"
            "おちゃ" -> "ocha.mp3"
            "ごはん" -> "gohan.mp3"
            else -> "${word.lowercase()}.mp3"
        }

        return AudioResponse(
            audioUrl = "/api/audio/file/$fileName",
            word = word,
            phonetics = getPhoneticsForWord(word)
        )
    }

    fun getAudioFile(fileName: String): Resource {
        // In a real implementation, you would validate the fileName and ensure it exists
        return resourceLoader.getResource("classpath:audio/$fileName")
    }

    private fun getPhoneticsForWord(word: String): String? {
        // In a real implementation, you would look up phonetics from a database or API
        return when (word) {
            "ください" -> "ku-da-sa-i"
            "おちゃ" -> "o-cha"
            "ごはん" -> "go-ha-n"
            else -> null
        }
    }
}