package com.example.deepsea.service

import com.example.deepsea.model.LanguageOption
import com.example.deepsea.model.SurveyOption
import com.example.deepsea.model.User
import com.example.deepsea.model.UserProfile
import com.example.deepsea.repository.UserProfileRepository
import com.example.deepsea.repository.UserRepository
import jakarta.transaction.Transactional

import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun findByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email)
    }


    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun existsByName(username: String): Boolean{
        return userRepository.existsByUsername(username)
    }

    fun getAllUsers() : List<User>{
        return userRepository.findAll()
    }

    fun delete(user: User) {
        userRepository.delete(user)
    }
    fun getUserProfile(userId: Long): UserProfile {
        val user = userRepository.findById(userId).orElseThrow { Exception("User not found") }
        return user.profile ?: throw Exception("Profile not found")
    }

    fun updateFirstLoginStatus(userId: Long) {
        val user = userRepository.findById(userId).orElseThrow { Exception("User not found") }
        if (user.firstLogin) {
            user.firstLogin = false
            userRepository.save(user)
        }
    }
    @Transactional
    fun updateUserSurveySelections(userId: Long, surveySelections: Set<SurveyOption>): UserProfile {
        val userProfile = userProfileRepository.findByUserId(userId)
            ?: throw IllegalStateException("User profile not found")

        userProfile.selectedSurveys = surveySelections
        return userProfileRepository.save(userProfile)
    }

    fun updateUserLanguageSelections(userId: Long, languageSelections: Set<LanguageOption>): UserProfile? {
        val userProfile = userProfileRepository.findByUserId(userId)
            ?: throw IllegalStateException("User profile not found")

        userProfile.courses = languageSelections
        return userProfileRepository.save(userProfile)
    }

}
