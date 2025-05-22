package com.example.deepsea.service

import com.example.deepsea.entity.FriendSuggestion
import com.example.deepsea.entity.UserProfile
import com.example.deepsea.repository.FriendSuggestionRepository
import com.example.deepsea.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendSuggestionService(
    private val userProfileRepository: UserProfileRepository,
    private val friendSuggestionRepository: FriendSuggestionRepository
) {
    /**
     * Generate friend suggestions for a user based on common languages, learning paths,
     * and activity level
     */
    @Transactional
    fun generateSuggestionsForUser(userId: Long, maxSuggestions: Int = 5): List<FriendSuggestion> {
        val userProfile = userProfileRepository.findByUserId(userId)
            ?: throw NoSuchElementException("User profile not found for user ID: $userId")

        // Find potential friends (exclude existing friends and self)
        val potentialFriends = userProfileRepository.findAll()
            .filter { profile ->
                profile.id != userProfile.id &&
                        profile !in userProfile.friends
            }

        // Calculate similarity scores and sort
        val scoredSuggestions = potentialFriends.map { profile ->
            // Calculate similarity score based on multiple factors
            val languageSimilarity = calculateLanguageSimilarity(userProfile, profile)
            val surveySimilarity = calculateSurveySimilarity(userProfile, profile)
            val activitySimilarity = calculateActivitySimilarity(userProfile, profile)

            // Weighted score (can adjust weights as needed)
            val finalScore = (languageSimilarity * 0.5) +
                    (surveySimilarity * 0.3) +
                    (activitySimilarity * 0.2)

            // Create suggestion reason
            val reason = when {
                languageSimilarity > 0.7 -> "Learning similar languages"
                surveySimilarity > 0.7 -> "Has similar learning goals"
                activitySimilarity > 0.7 -> "Has similar activity level"
                else -> "Might be a good learning partner"
            }

            // Create suggestion entity
            FriendSuggestion(
                userProfile = userProfile,
                suggestedProfile = profile,
                similarityScore = finalScore,
                suggestionReason = reason
            )
        }.sortedByDescending { it.similarityScore }

        // Take top N suggestions, but check if they already exist in DB
        val topSuggestions = scoredSuggestions
            .filter { suggestion ->
                !friendSuggestionRepository.existsByUserProfileAndSuggestedProfile(
                    userProfile, suggestion.suggestedProfile
                )
            }
            .take(maxSuggestions)

        // Save all new suggestions
        return friendSuggestionRepository.saveAll(topSuggestions)
    }

    /**
     * Mark a suggestion as followed (user added this friend)
     */
    @Transactional
    fun markSuggestionAsFollowed(suggestionId: Long): Boolean {
        val suggestion = friendSuggestionRepository.findById(suggestionId)
            .orElse(null) ?: return false

        suggestion.isFollowed = true
        friendSuggestionRepository.save(suggestion)

        // Here we could add actual friend connection logic
        // userProfile.friends += suggestedProfile
        // userProfileRepository.save(userProfile)

        return true
    }

    /**
     * Dismiss a suggestion (user doesn't want to see it)
     */
    @Transactional
    fun dismissSuggestion(suggestionId: Long): Boolean {
        val suggestion = friendSuggestionRepository.findById(suggestionId)
            .orElse(null) ?: return false

        suggestion.isDismissed = true
        friendSuggestionRepository.save(suggestion)
        return true
    }

    /**
     * Get active (not dismissed or followed) suggestions for a user
     */
    fun getActiveSuggestionsForUser(userId: Long): List<FriendSuggestion> {
        val userProfile = userProfileRepository.findByUserId(userId)
            ?: throw NoSuchElementException("User profile not found for user ID: $userId")

        return friendSuggestionRepository
            .findByUserProfileAndIsDismissedFalseAndIsFollowedFalseOrderBySimilarityScoreDesc(userProfile)
    }

    /**
     * Calculate similarity between users based on language courses
     */
    private fun calculateLanguageSimilarity(user1: UserProfile, user2: UserProfile): Double {
        val languagesUser1 = user1.courses
        val languagesUser2 = user2.courses

        if (languagesUser1.isEmpty() || languagesUser2.isEmpty()) return 0.0

        val commonLanguages = languagesUser1.intersect(languagesUser2)
        // Jaccard similarity: intersection size / union size
        return commonLanguages.size.toDouble() /
                (languagesUser1.size + languagesUser2.size - commonLanguages.size)
    }

    /**
     * Calculate similarity based on selected surveys (learning interests)
     */
    private fun calculateSurveySimilarity(user1: UserProfile, user2: UserProfile): Double {
        val surveysUser1 = user1.selectedSurveys
        val surveysUser2 = user2.selectedSurveys

        if (surveysUser1.isEmpty() || surveysUser2.isEmpty()) return 0.0

        val commonSurveys = surveysUser1.intersect(surveysUser2)
        return commonSurveys.size.toDouble() /
                (surveysUser1.size + surveysUser2.size - commonSurveys.size)
    }

    /**
     * Calculate similarity based on user activity metrics
     */
    private fun calculateActivitySimilarity(user1: UserProfile, user2: UserProfile): Double {
        // Normalize each metric to 0-1 range
        val xpSimilarity = 1.0 - (kotlin.math.abs(user1.totalXp - user2.totalXp).toDouble() /
                kotlin.math.max(user1.totalXp, user2.totalXp).coerceAtLeast(1))

        val streakSimilarity = 1.0 - (kotlin.math.abs(user1.dayStreak - user2.dayStreak).toDouble() /
                kotlin.math.max(user1.dayStreak, user2.dayStreak).coerceAtLeast(1))

        // Return average of the normalized metrics
        return (xpSimilarity + streakSimilarity) / 2.0
    }
}