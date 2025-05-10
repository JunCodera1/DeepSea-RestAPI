package com.example.deepsea.service

import com.example.deepsea.repository.LessonRepository
import com.example.deepsea.repository.UnitRepository
import com.example.deepsea.repository.UserProgressRepository
import com.example.deepsea.repository.UserStatsRepository
import org.springframework.stereotype.Service

@Service
class UnitServiceWithStats(
    private val unitRepository: UnitRepository,
    private val lessonRepository: LessonRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userStatsRepository: UserStatsRepository
) : UnitService(unitRepository, lessonRepository, userProgressRepository, userStatsRepository)