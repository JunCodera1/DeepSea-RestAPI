package com.example.deepsea.repository

import com.example.deepsea.model.FriendSuggestion
import com.example.deepsea.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FriendSuggestionRepository : JpaRepository<FriendSuggestion, Long> {
    // Find all suggestions for a specific user profile
    fun findByUserProfileAndIsDismissedFalseAndIsFollowedFalseOrderBySimilarityScoreDesc(userProfile: UserProfile): List<FriendSuggestion>

    // Check if a specific suggestion already exists
    fun existsByUserProfileAndSuggestedProfile(userProfile: UserProfile, suggestedProfile: UserProfile): Boolean

    // Delete suggestions when a user follows/adds the suggested friend
    fun deleteByUserProfileAndSuggestedProfile(userProfile: UserProfile, suggestedProfile: UserProfile)

    // Find suggestions based on shared language interests
    @Query("""
        SELECT fs FROM FriendSuggestion fs 
        WHERE fs.userProfile = :userProfile 
        AND fs.isDismissed = false 
        AND fs.isFollowed = false
        AND EXISTS (
            SELECT 1 FROM UserProfile up1 JOIN up1.courses c1, UserProfile up2 JOIN up2.courses c2
            WHERE up1 = fs.userProfile AND up2 = fs.suggestedProfile AND c1 = c2
        )
        ORDER BY fs.similarityScore DESC
    """)
    fun findSuggestionsWithSharedLanguages(userProfile: UserProfile): List<FriendSuggestion>
}