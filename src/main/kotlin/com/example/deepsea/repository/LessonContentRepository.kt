package com.example.deepsea.repository

import com.example.deepsea.model.LessonContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LessonContentRepository : JpaRepository<LessonContent, Long> {
    fun findByLessonIdOrderByOrderIndex(lessonId: Long): List<LessonContent>

    @Query("SELECT lc FROM LessonContent lc WHERE lc.lessonId = :lessonId AND lc.contentType = :contentType ORDER BY lc.orderIndex")
    fun findByLessonIdAndContentType(lessonId: Long, contentType: String): List<LessonContent>

    fun deleteByLessonId(lessonId: Long)
}