package com.example.deepsea.controller

import com.example.deepsea.entity.Story
import com.example.deepsea.service.StoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stories")
class StoryController(private val storyService: StoryService) {
    @GetMapping
    fun getAllStories(): List<Story> = storyService.getAllStories()
}