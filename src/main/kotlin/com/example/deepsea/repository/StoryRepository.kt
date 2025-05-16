package com.example.deepsea.repository

import com.example.deepsea.model.Story
import org.springframework.data.jpa.repository.JpaRepository

interface StoryRepository : JpaRepository<Story, Long>