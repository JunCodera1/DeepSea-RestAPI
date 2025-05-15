package com.example.deepsea.controller


import com.example.deepsea.dto.FriendSuggestionResponseDto
import com.example.deepsea.mapper.FriendSuggestionMapper
import com.example.deepsea.service.FriendSuggestionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/friend-suggestions")
class FriendSuggestionController(
    private val friendSuggestionService: FriendSuggestionService,
    private val friendSuggestionMapper: FriendSuggestionMapper
) {
    /**
     * Get active friend suggestions for a user
     */
    @GetMapping("/user/{userId}")
    fun getSuggestionsForUser(@PathVariable userId: Long): ResponseEntity<FriendSuggestionResponseDto> {
        val suggestions = friendSuggestionService.getActiveSuggestionsForUser(userId)
        val suggestionDtos = suggestions.map { friendSuggestionMapper.toDto(it) }

        return ResponseEntity.ok(FriendSuggestionResponseDto(
            suggestions = suggestionDtos,
            count = suggestionDtos.size
        ))
    }

    /**
     * Generate new suggestions for a user
     */
    @PostMapping("/generate/{userId}")
    fun generateSuggestions(
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "5") maxSuggestions: Int
    ): ResponseEntity<FriendSuggestionResponseDto> {
        val suggestions = friendSuggestionService.generateSuggestionsForUser(userId, maxSuggestions)
        val suggestionDtos = suggestions.map { friendSuggestionMapper.toDto(it) }

        return ResponseEntity.ok(FriendSuggestionResponseDto(
            suggestions = suggestionDtos,
            count = suggestionDtos.size
        ))
    }

    /**
     * Mark a suggestion as followed (user added this friend)
     */
    @PostMapping("/{suggestionId}/follow")
    fun followSuggestion(@PathVariable suggestionId: Long): ResponseEntity<Map<String, Boolean>> {
        val result = friendSuggestionService.markSuggestionAsFollowed(suggestionId)
        return ResponseEntity.ok(mapOf("success" to result))
    }

    /**
     * Dismiss a suggestion
     */
    @PostMapping("/{suggestionId}/dismiss")
    fun dismissSuggestion(@PathVariable suggestionId: Long): ResponseEntity<Map<String, Boolean>> {
        val result = friendSuggestionService.dismissSuggestion(suggestionId)
        return ResponseEntity.ok(mapOf("success" to result))
    }
}