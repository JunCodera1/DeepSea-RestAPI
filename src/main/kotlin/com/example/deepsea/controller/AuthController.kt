package com.example.deepsea.controller

import RegisterResponseDto
import com.example.deepsea.dto.LoginRequestDto
import com.example.deepsea.dto.LoginResponseDto
import com.example.deepsea.exception.ApiException
import com.example.deepsea.model.User
import com.example.deepsea.model.UserProfile
import com.example.deepsea.service.HashService
import com.example.deepsea.service.TokenService
import com.example.deepsea.service.UserProfileService
import com.example.deepsea.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val hashService: HashService,
    private val tokenService: TokenService,
    private val userService: UserService,
    private val userProfileService: UserProfileService
) {

    @GetMapping("/users")
    fun users(): List<User> {
        return userService.getAllUsers()
    }

    // Lấy user theo ID
    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Long): User {
        return userService.getUserById(id) ?: throw ApiException(404, "User with id=$id not found")
    }

    @PostMapping("/login")
    fun login(@RequestBody payload: LoginRequestDto): LoginResponseDto {
        val user = userService.findByEmail(payload.email) ?: throw ApiException(400, "Login failed")

        if (!hashService.checkBcrypt(payload.password, user.password)) {
            throw ApiException(400, "Login failed")
        }

        return LoginResponseDto(
            id = user.id,
            token = tokenService.createToken(user),
            username = user.username,
            email = user.getEmail()
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterResponseDto): RegisterResponseDto {
        if (userService.existsByName(dto.username)) {
            throw ApiException(400, "Name already exists")
        }

        // Tạo user
        val user = User(
            name = dto.name,
            username = dto.username,
            password = hashService.hashBcrypt(dto.password.toString()),
            email = dto.email,
            avatarUrl = dto.avatarUrl
        )
        val savedUser = userService.save(user)

        val profile = UserProfile(
            name = dto.name ?: dto.username,
            username = dto.username,
            user = savedUser,
            followers = 0,
            following = 0,
            dayStreak = 0,
            totalXp = 0,
            currentLeague = "Bronze",
            topFinishes = 0
        )
        userProfileService.saveUserProfile(profile)

        // Trả về RegisterResponseDto
        return RegisterResponseDto(
            id = savedUser.id,
            name = savedUser.getName(),
            username = savedUser.getUsername(),
            email = savedUser.getEmail(),
            avatarUrl = savedUser.avatarUrl,
            password = ""
        )
    }




    @DeleteMapping("/delete-user/{id}")
    fun deleteUser(@PathVariable id: Long): String {
        val user = userService.getUserById(id)
        if (user != null) {
            userService.delete(user)
        }
        return "User with id=$id deleted successfully"
    }

}