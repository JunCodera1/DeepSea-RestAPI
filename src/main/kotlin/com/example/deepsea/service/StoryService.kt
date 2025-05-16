package com.example.deepsea.service

import com.example.deepsea.model.Story
import com.example.deepsea.repository.StoryRepository
import org.springframework.stereotype.Service

@Service
class StoryService(private val storyRepository: StoryRepository) {
    fun getAllStories(): List<Story> = storyRepository.findAll()
}