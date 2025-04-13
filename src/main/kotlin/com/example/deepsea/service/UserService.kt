package com.example.deepsea.service

import com.example.deepsea.model.User
import com.example.deepsea.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Service
class UserService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
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

}
