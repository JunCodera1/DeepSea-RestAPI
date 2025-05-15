package com.example.deepsea.controller

import com.example.deepsea.dto.*
import com.example.deepsea.model.Match
import com.example.deepsea.model.Question
import com.example.deepsea.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game")
class GameController(private val gameService: GameService) {

    @PostMapping("/start")
    fun startMatch(@RequestBody request: GameStartRequest): ResponseEntity<Match> {
        return try {
            val match = gameService.startMatch(request)
            ResponseEntity.ok(match)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null)
        } catch (e: Exception) {
            e.printStackTrace() // THÊM DÒNG NÀY để log lỗi ra console
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }


    @PostMapping("/answer")
    fun submitAnswer(@RequestBody request: GameAnswerRequest): ResponseEntity<AnswerResponse> {
        return try {
            val response = gameService.submitAnswer(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @GetMapping("/questions/{matchId}")
    fun getMatchQuestions(@PathVariable matchId: Long): ResponseEntity<List<QuestionDto>> {
        return try {
            val questions = gameService.getMatchQuestions(matchId)
            ResponseEntity.ok(questions)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(emptyList())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }
}