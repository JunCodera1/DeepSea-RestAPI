package com.example.deepsea.controller

import RegisterDto
import com.example.deepsea.dto.LoginDto
import com.example.deepsea.exception.ApiException
import com.example.deepsea.model.User
import com.example.deepsea.service.HashService
import com.example.deepsea.service.TokenService
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
    fun login(@RequestBody payload: LoginDto): LoginDto {
        val user = userService.findByEmail(payload.email) ?: throw ApiException(400, "Login failed")

        if (!hashService.checkBcrypt(payload.password, user.password)) {
            throw ApiException(400, "Login failed")
        }

        return LoginDto(
            token = tokenService.createToken(user),
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterDto): LoginDto {
        if (userService.existsByName(dto.username)) {
            throw ApiException(400, "Name already exists")
        }

        val user = User(
            username = dto.username,
            password = hashService.hashBcrypt(dto.password),
            email = dto.email,
            avatarUrl = dto.avatarUrl
        )
        val savedUser = userService.save(user)
        return LoginDto(token = tokenService.createToken(savedUser))
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