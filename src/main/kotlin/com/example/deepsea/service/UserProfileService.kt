package com.example.deepsea.service

import com.example.deepsea.dto.SurveyOption
import com.example.deepsea.dto.UserProfileDto
import com.example.deepsea.exception.UserNotFoundException
import com.example.deepsea.exception.UserProfileNotFoundException
import com.example.deepsea.model.UserProfile
import com.example.deepsea.repository.UserProfileRepository
import com.example.deepsea.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) {

    fun getUserProfile(userId: Long): UserProfile {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found")
        return user.profile ?: throw UserProfileNotFoundException("Profile not found")
    }

    fun getUserProfileData(userId: Long): UserProfileDto {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found")
        val profile = user.profile ?: throw UserProfileNotFoundException("Profile not found")

        return UserProfileDto(
            name = profile.name,
            username = user.username,
            joinDate = profile.joinDate,
            courses = profile.courses,
            followers = profile.followers,
            following = profile.following,
            dayStreak = profile.dayStreak,
            totalXp = profile.totalXp,
            currentLeague = profile.currentLeague,
            topFinishes = profile.topFinishes,
            friends = profile.friends
        )
    }
    fun saveUserProfile(profile: UserProfile): UserProfile {
        return userProfileRepository.save(profile)
    }

    fun updateSurveySelections(profile: UserProfile, surveys: Set<SurveyOption>): UserProfile {
        val updatedProfile = profile.copy(selectedSurveys = surveys)
        return userProfileRepository.save(updatedProfile)
    }
}
