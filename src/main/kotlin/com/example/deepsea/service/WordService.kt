package com.example.deepsea.service

import com.example.deepsea.model.Word
import com.example.deepsea.repository.WordRepository
import org.springframework.stereotype.Service

@Service
class WordService(private val wordRepository: WordRepository) {
    fun getAllWords(): List<Word> = wordRepository.findAll()
    fun getWordsByTheme(theme: String): List<Word> = wordRepository.findByTheme(theme)
}