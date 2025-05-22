package com.example.deepsea.controller

import com.example.deepsea.dto.LeaderboardEntryDto
import com.example.deepsea.entity.UserProfile
import com.example.deepsea.repository.UserProfileRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/leaderboard")
class LeaderboardController(
    private val userProfileRepository: UserProfileRepository
) {
    @GetMapping("/top")
    fun getTopUsers(): List<LeaderboardEntryDto> {
        return userProfileRepository.findTop10ByOrderByTotalXpDesc()
            .map {
                LeaderboardEntryDto(
                    username = it.username,
                    name = it.name,
                    totalXp = it.totalXp,
                    currentLeague = it.currentLeague,
                    topFinishes = it.topFinishes,
                    dayStreak = it.dayStreak
                )
            }
    }

    @GetMapping("/all")
    fun getAllUsersSortedByXp(): List<UserProfile> {
        return userProfileRepository.findAllByOrderByTotalXpDesc()
    }

    @GetMapping("/rank")
    fun getUserRank(@RequestParam userId: Long): Map<String, Any> {
        val userProfile = userProfileRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }

        val rank = userProfileRepository.findUserRankByXp(userProfile.totalXp)

        return mapOf(
            "userId" to userId,
            "username" to userProfile.username,
            "rank" to rank,
            "totalXp" to userProfile.totalXp
        )
    }
}
