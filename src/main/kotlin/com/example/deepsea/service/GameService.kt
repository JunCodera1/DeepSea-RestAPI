package com.example.deepsea.service

import com.example.deepsea.dto.*
import com.example.deepsea.model.*
import com.example.deepsea.repository.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

@Service
class GameService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val questionRepository: QuestionRepository,
    private val matchRepository: MatchRepository,
    private val matchQuestionRepository: MatchQuestionRepository,
    private val matchAnswerRepository: MatchAnswerRepository,
    private val leaderboardRepository: LeaderboardRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun startMatch(request: GameStartRequest): Match {
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        var match = Match(
            player1Id = request.userId,
            player2Id = null,
            gameMode = request.gameMode,
            status = "PENDING"
            // ❌ KHÔNG gán id ở đây
        )
        match = matchRepository.save(match)

        val questions = questionRepository.findRandomQuestionsByModeAndLanguage(
            gameMode = request.gameMode,
            language = request.language,
            count = 5
        )

        questions.forEachIndexed { index, question ->
            matchQuestionRepository.save(
                MatchQuestion(
                    matchId = match.id,
                    questionId = question.id,
                    questionOrder = index + 1
                )
            )
        }

        match.status = "IN_PROGRESS"
        matchRepository.save(match)

        return match
    }

    @Transactional
    fun submitAnswer(request: GameAnswerRequest): AnswerResponse {
        val match = matchRepository.findById(request.matchId)
            .orElseThrow { IllegalArgumentException("Match not found") }
        val question = questionRepository.findById(request.questionId)
            .orElseThrow { IllegalArgumentException("Question not found") }
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val isCorrect = request.selectedAnswer == question.correctAnswer
        val xpEarned = if (isCorrect) 100 else 0

        val answer = MatchAnswer(
            matchId = request.matchId,
            questionId = request.questionId,
            userId = request.userId,
            selectedAnswer = request.selectedAnswer,
            isCorrect = isCorrect
        )
        matchAnswerRepository.save(answer)

        user.profile?.let { profile ->
            profile.totalXp += xpEarned
            userProfileRepository.save(profile)
        }

        val totalQuestions = matchQuestionRepository.countByMatchId(request.matchId)
        val answeredQuestions = matchAnswerRepository.countByMatchIdAndUserId(request.matchId, request.userId)

        var matchCompleted = false
        if (answeredQuestions >= totalQuestions) {
            var updatedMatch = match
            updatedMatch.status = "COMPLETED"
            updatedMatch.completedAt = LocalDateTime.now()
            matchRepository.save(updatedMatch)
            matchCompleted = true

            updateLeaderboard(request.userId)
        }

        return AnswerResponse(
            isCorrect = isCorrect,
            matchCompleted = matchCompleted,
            xpEarned = xpEarned
        )
    }

    private fun updateLeaderboard(userId: Long) {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val score = user.profile?.totalXp ?: 0
        val weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))

        val existingEntry = leaderboardRepository.findByUserIdAndWeekStartDate(userId, weekStart)
        val leaderboardEntry = existingEntry?.apply {
            this.score = score
        } ?: Leaderboard(
            userId = userId,
            score = score,
            rank = 0,
            weekStartDate = weekStart
        )
        leaderboardRepository.save(leaderboardEntry)

        val entries = leaderboardRepository.findByWeekStartDateOrderByScoreDesc(weekStart)
        entries.forEachIndexed { index, entry ->
            entry.rank = index + 1
            leaderboardRepository.save(entry)
        }
    }

    @Transactional(readOnly = true)
    fun getMatchQuestions(matchId: Long): List<QuestionDto> {
        val match = matchRepository.findById(matchId)
            .orElseThrow { IllegalArgumentException("Match not found") }

        return matchQuestionRepository.findByMatchId(matchId)
            .map { matchQuestion ->
                val question = questionRepository.findById(matchQuestion.questionId)
                    .orElseThrow { IllegalArgumentException("Question ${matchQuestion.questionId} not found") }
                QuestionDto(
                    id = question.id,
                    text = question.text,
                    options = objectMapper.readValue(question.options, object : TypeReference<List<String>>() {}),
                    correctAnswer = question.correctAnswer,
                    gameMode = question.gameMode,
                    language = question.language
                )
            }
    }
}