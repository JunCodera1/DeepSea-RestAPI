package com.example.deepsea.controller

import com.example.deepsea.dto.LoginDto
import com.example.deepsea.dto.RegisterDto
import com.example.deepsea.exception.ApiException
import com.example.deepsea.model.User
import com.example.deepsea.service.HashService
import com.example.deepsea.service.TokenService
import com.example.deepsea.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val hashService: HashService,
    private val tokenService: TokenService,
    private val userService: UserService,
) {

    @GetMapping("/users")
    fun users(): List<User> {
        return userService.getAllUsers()
    }

    // Lấy user theo ID
    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Long): User {
        return userService.getUserById(id)
    }

    @PostMapping("/login")
    fun login(@RequestBody payload: LoginDto): LoginDto {
        val user = userService.findByUsername(payload.name) ?: throw ApiException(400, "Login failed")

        if (!hashService.checkBcrypt(payload.password, user.password)) {
            throw ApiException(400, "Login failed")
        }

        return LoginDto(
            token = tokenService.createToken(user),
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody payload: List<RegisterDto>): List<LoginDto> {
        return payload.map { dto ->
            if (userService.existsByName(dto.username)) {
                throw ApiException(400, "Name already exists")
            }
            val user = User(
                username = dto.username,
                password = hashService.hashBcrypt(dto.password)
            )
            val savedUser = userService.save(user)
            LoginDto(token = tokenService.createToken(savedUser))
        }
    }
}