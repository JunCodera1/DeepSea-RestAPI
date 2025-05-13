package com.example.deepsea.controller

import RegisterResponseDto
import com.example.deepsea.dto.GoogleTokenRequestDto
import com.example.deepsea.dto.LoginRequestDto
import com.example.deepsea.dto.LoginResponseDto
import com.example.deepsea.exception.ApiException
import com.example.deepsea.model.User
import com.example.deepsea.model.UserProfile
import com.example.deepsea.service.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val hashService: HashService,
    private val tokenService: TokenService,
    private val userService: UserService,
    private val userProfileService: UserProfileService,
    private val googleAuthService: GoogleAuthService
) {

    @GetMapping("/users")
    fun users(): List<User> {
        return userService.getAllUsers()
    }

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Long): User {
        return userService.getUserById(id) ?: throw ApiException(404, "User with id=$id not found")
    }

    @PostMapping("/login")
    fun login(@RequestBody payload: LoginRequestDto): LoginResponseDto {
        val user = userService.findByEmail(payload.email)
            .orElseThrow { ApiException(400, "Login failed") } // Nếu không tìm thấy user, ném exception
        val today = LocalDate.now()
        val lastLogin = user.lastLogin

        user.profile!!.dayStreak = when {
            lastLogin == null -> 1
            ChronoUnit.DAYS.between(lastLogin, today) == 1L -> user.profile.dayStreak + 1
            ChronoUnit.DAYS.between(lastLogin, today) > 1L -> 1
            else -> user.profile.dayStreak // login cùng ngày, không thay đổi
        }
        // Kiểm tra mật khẩu
        if (!hashService.checkBcrypt(payload.password, user.password)) {
            throw ApiException(400, "Login failed")
        }

        val isFirstLogin = user.firstLogin
        if (isFirstLogin) {
            userService.updateFirstLoginStatus(user.id!!)
        }

        // Trả về thông tin đăng nhập
        return LoginResponseDto(
            id = user.id!!,
            token = tokenService.createToken(user),
            username = user.username,
            email = user.getEmail(),
            firstLogin = isFirstLogin,
            profile_id = user.profile!!.id
        )
    }


    @PostMapping("/register")
    fun register(@RequestBody dto: RegisterResponseDto): RegisterResponseDto {
        if (userService.existsByName(dto.username)) {
            throw ApiException(400, "Name already exists")
        }

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

        return RegisterResponseDto(
            id = savedUser.id,
            name = savedUser.getName(),
            username = savedUser.getUsername(),
            email = savedUser.getEmail(),
            avatarUrl = savedUser.avatarUrl,
            password = ""
        )
    }

    @PostMapping("/google")
    fun loginWithGoogle(@RequestBody payload: GoogleTokenRequestDto): LoginResponseDto {
        val googleUserInfo = googleAuthService.verifyGoogleToken(payload.token)
            ?: throw ApiException(400, "Invalid Google token")

        val user = userService.findByEmail(googleUserInfo.email).orElse(null)

        val loggedInUser = if (user != null) {

            userService.save(user)
        } else {
            // Tạo mới user từ thông tin Google
            val newUser = User(
                name = googleUserInfo.name,
                username = generateUniqueUsername(googleUserInfo.name),
                password = hashService.hashBcrypt(generateRandomPassword()),
                email = googleUserInfo.email,
                avatarUrl = googleUserInfo.picture
            )
            val savedUser = userService.save(newUser)

            // Tạo hồ sơ người dùng mới
            val profile = UserProfile(
                name = googleUserInfo.name,
                username = newUser.username,
                user = savedUser,
                followers = 0,
                following = 0,
                dayStreak = 0,
                totalXp = 0,
                currentLeague = "Bronze",
                topFinishes = 0
            )
            userProfileService.saveUserProfile(profile)

            savedUser
        }

        // Đảm bảo cập nhật streak và trạng thái đăng nhập
        val today = LocalDate.now()
        val lastLogin = loggedInUser.lastLogin

        loggedInUser.profile!!.dayStreak = when {
            lastLogin == null -> 1
            ChronoUnit.DAYS.between(lastLogin, today) == 1L -> loggedInUser.profile.dayStreak + 1
            ChronoUnit.DAYS.between(lastLogin, today) > 1L -> 1
            else -> loggedInUser.profile.dayStreak
        }

        val isFirstLogin = loggedInUser.firstLogin
        if (isFirstLogin) {
            userService.updateFirstLoginStatus(loggedInUser.id!!)
        }

        // Trả về thông tin đăng nhập
        return LoginResponseDto(
            id = loggedInUser.id!!,
            token = tokenService.createToken(loggedInUser),
            username = loggedInUser.username,
            email = loggedInUser.getEmail(),
            firstLogin = isFirstLogin,
            profile_id = loggedInUser.profile!!.id
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

    // Helper method to generate unique username if the one from Google already exists
    private fun generateUniqueUsername(name: String): String {
        val baseUsername = name.lowercase().replace("\\s+".toRegex(), "")
        var username = baseUsername
        var counter = 1

        while (userService.existsByName(username)) {
            username = baseUsername + counter
            counter++
        }

        return username
    }

    // Helper method to generate random password for Google users
    private fun generateRandomPassword(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..16).map { allowedChars.random() }.joinToString("")
    }

}