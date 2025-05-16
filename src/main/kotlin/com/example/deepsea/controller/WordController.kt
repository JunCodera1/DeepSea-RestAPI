package com.example.deepsea.controller

import com.example.deepsea.model.Word
import com.example.deepsea.service.WordService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/words")
class WordController(private val wordService: WordService) {

    @GetMapping
    fun getAllWords(): List<Word> = wordService.getAllWords()

    @GetMapping("/by-theme")
    fun getWordsByTheme(@RequestParam theme: String): List<Word> = wordService.getWordsByTheme(theme)
}