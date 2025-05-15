package com.example.deepsea.repository

import com.example.deepsea.model.LessonResult
import org.springframework.data.jpa.repository.JpaRepository

interface LessonResultRepository : JpaRepository<LessonResult, Long>