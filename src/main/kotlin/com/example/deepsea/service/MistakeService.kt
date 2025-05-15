package com.example.deepsea.service

import com.example.deepsea.dto.MistakeRequest
import com.example.deepsea.dto.MistakeResponse
import com.example.deepsea.model.Mistake
import com.example.deepsea.repository.MistakeRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class MistakeService(private val mistakeRepository: MistakeRepository) {

    fun saveMistake(mistakeRequest: MistakeRequest): MistakeResponse {
        // Kiểm tra xem lỗi này đã tồn tại chưa
        val existingMistakes = mistakeRepository.findByUserIdAndWord(
            mistakeRequest.userId,
            mistakeRequest.word
        )

        val mistake = if (existingMistakes.isNotEmpty()) {
            val existing = existingMistakes.first()
            existing.copy(
                userAnswer = mistakeRequest.userAnswer,
                createdAt = LocalDateTime.now(),
                reviewCount = existing.reviewCount
            )
        } else {
            Mistake(
                userId = mistakeRequest.userId,
                word = mistakeRequest.word,
                correctAnswer = mistakeRequest.correctAnswer,
                userAnswer = mistakeRequest.userAnswer,
                lessonId = mistakeRequest.lessonId
            )
        }

        val savedMistake = mistakeRepository.save(mistake)

        return MistakeResponse(
            id = savedMistake.id,
            word = savedMistake.word,
            correctAnswer = savedMistake.correctAnswer,
            userAnswer = savedMistake.userAnswer,
            createdAt = savedMistake.createdAt.format(DateTimeFormatter.ISO_DATE_TIME),
            reviewCount = savedMistake.reviewCount
        )
    }

    fun getMistakesByUserId(userId: Long): List<MistakeResponse> {
        return mistakeRepository.findByUserId(userId).map { mistake ->
            MistakeResponse(
                id = mistake.id,
                word = mistake.word,
                correctAnswer = mistake.correctAnswer,
                userAnswer = mistake.userAnswer,
                createdAt = mistake.createdAt.format(DateTimeFormatter.ISO_DATE_TIME),
                reviewCount = mistake.reviewCount
            )
        }
    }

    fun markAsReviewed(mistakeId: Long): MistakeResponse {
        val mistake = mistakeRepository.findById(mistakeId)
            .orElseThrow { RuntimeException("Mistake not found with id: $mistakeId") }

        val updatedMistake = mistake.copy(
            reviewedAt = LocalDateTime.now(),
            reviewCount = mistake.reviewCount + 1
        )

        val savedMistake = mistakeRepository.save(updatedMistake)

        return MistakeResponse(
            id = savedMistake.id,
            word = savedMistake.word,
            correctAnswer = savedMistake.correctAnswer,
            userAnswer = savedMistake.userAnswer,
            createdAt = savedMistake.createdAt.format(DateTimeFormatter.ISO_DATE_TIME),
            reviewCount = savedMistake.reviewCount
        )
    }

    fun deleteMistake(mistakeId: Long) {
        mistakeRepository.deleteById(mistakeId)
    }
}