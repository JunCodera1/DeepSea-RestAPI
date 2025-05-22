package com.example.deepsea.controller

import com.example.deepsea.annotations.AdminOnly
import com.example.deepsea.dto.*
import com.example.deepsea.entity.UserProfile
import com.example.deepsea.enums.Role
import com.example.deepsea.exception.ApiException
import com.example.deepsea.service.HashService
import com.example.deepsea.service.TokenService
import com.example.deepsea.service.UserProfileService
import com.example.deepsea.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val userService: UserService,
    private val userProfileService: UserProfileService,
    private val tokenService: TokenService,
    private val hashService: HashService
) {

    // Middleware để kiểm tra quyền admin
    @GetMapping("/check-admin")
    fun checkAdminAccess(@RequestHeader("Authorization") token: String): Map<String, Any> {
        val isValid = tokenService.validateToken(token)
        if (!isValid) {
            throw ApiException(401, "Invalid or expired token")
        }

        val role = tokenService.getRoleFromToken(token)
        val isAdmin = role == Role.ADMIN

        return mapOf(
            "isAdmin" to isAdmin,
            "role" to (role?.name ?: "UNKNOWN")
        )
    }

    // Lấy tất cả users (chỉ admin)
    @AdminOnly
    @GetMapping("/users")
    fun getAllUsers(): List<UserDto> {
        return userService.getAllUsers().map { user ->
            UserDto(
                id = user.id!!,
                name = user.getName(),
                username = user.username,
                email = user.getEmail(),
                role = user.role.name,
                avatarUrl = user.avatarUrl,
                firstLogin = user.firstLogin,
                lastLogin = user.lastLogin,
                createdAt = user.createdAt,
                profile = user.profile?.let { profile ->
                    UserProfileDto(
                        followers = profile.followers,
                        following = profile.following,
                        dayStreak = profile.dayStreak,
                        totalXp = profile.totalXp,
                        currentLeague = profile.currentLeague,
                        topFinishes = profile.topFinishes,
                        name = user.getName(),
                        username = user.username,
                        joinDate = profile.joinDate,
                        courses = profile.courses,
                        avatarUrl = user.avatarUrl,
                        friends = profile.friends,
                        dailyGoalOption = profile.dailyGoal,
                        streakHistory = profile.streakHistory
                    )
                }
            )
        }
    }

    // Lấy thông tin user cụ thể
    @GetMapping("/users/{id}")
    fun getUserById(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): UserDto {
        validateAdmin(token)

        val user = userService.getUserById(id)
            ?: throw ApiException(404, "User not found")

        return UserDto(
            id = user.id!!,
            name = user.getName(),
            username = user.username,
            email = user.getEmail(),
            role = user.role.name,
            avatarUrl = user.avatarUrl,
            firstLogin = user.firstLogin,
            lastLogin = user.lastLogin,
            createdAt = user.createdAt,
            profile = user.profile?.let { profile ->
                UserProfileDto(
                    followers = profile.followers,
                    following = profile.following,
                    dayStreak = profile.dayStreak,
                    totalXp = profile.totalXp,
                    currentLeague = profile.currentLeague,
                    topFinishes = profile.topFinishes,
                    name = user.getName(),
                    username = user.username,
                    joinDate = profile.joinDate,
                    courses = profile.courses,
                    avatarUrl = user.avatarUrl,
                    friends = profile.friends,
                    dailyGoalOption = profile.dailyGoal,
                    streakHistory = profile.streakHistory
                )
            }
        )
    }

    // Cập nhật role của user
    @AdminOnly
    @PutMapping("/users/{id}/role")
    fun updateUserRole(
        @PathVariable id: Long,
        @RequestBody roleDto: UpdateRoleDto
    ): Map<String, String> {
        val user = userService.getUserById(id)
            ?: throw ApiException(404, "User not found")

        val newRole = try {
            Role.valueOf(roleDto.role.uppercase())
        } catch (e: IllegalArgumentException) {
            throw ApiException(400, "Invalid role: ${roleDto.role}")
        }

        // Get current user from security context
        val currentUsername = SecurityContextHolder.getContext().authentication.name
        val currentUser = userService.findByUsername(currentUsername)

        if (currentUser?.id == id && newRole != Role.ADMIN) {
            throw ApiException(400, "Cannot demote yourself")
        }

        user.role = newRole
        userService.save(user)

        return mapOf(
            "message" to "User role updated successfully",
            "userId" to id.toString(),
            "newRole" to newRole.name
        )
    }

    @AdminOnly
    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Long): Map<String, String> {
        val user = userService.getUserById(id)
            ?: throw ApiException(404, "User not found")

        val currentUsername = SecurityContextHolder.getContext().authentication.name
        val currentUser = userService.findByUsername(currentUsername)

        if (currentUser?.id == id) {
            throw ApiException(400, "Cannot delete yourself")
        }

        userService.delete(user)

        return mapOf(
            "message" to "User deleted successfully",
            "deletedUserId" to id.toString()
        )
    }

    @AdminOnly
    @GetMapping("/stats")
    fun getSystemStats(): SystemStatsDto {
        val allUsers = userService.getAllUsers()
        val totalUsers = allUsers.size
        val adminUsers = allUsers.count { it.role == Role.ADMIN }
        val regularUsers = allUsers.count { it.role == Role.USER }
        val activeUsersToday = allUsers.count { it.lastLogin == LocalDate.now() }
        val newUsersThisWeek = allUsers.count {
            it.createdAt?.isAfter(LocalDate.now().minusDays(7)) == true
        }

        return SystemStatsDto(
            totalUsers = totalUsers,
            adminUsers = adminUsers,
            regularUsers = regularUsers,
            activeUsersToday = activeUsersToday,
            newUsersThisWeek = newUsersThisWeek,
            totalProfiles = allUsers.count { it.profile != null }
        )
    }

    // Tìm kiếm users
    @GetMapping("/users/search")
    fun searchUsers(
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) role: String?,
        @RequestHeader("Authorization") token: String
    ): List<UserDto> {
        validateAdmin(token)

        var users = userService.getAllUsers()

        username?.let { users = users.filter { it.username.contains(username, ignoreCase = true) } }
        email?.let { users = users.filter { it.getEmail().contains(email, ignoreCase = true) } }
        role?.let {
            val roleEnum = try {
                Role.valueOf(role.uppercase())
            } catch (e: IllegalArgumentException) {
                throw ApiException(400, "Invalid role: $role")
            }
            users = users.filter { it.role == roleEnum }
        }

        return users.map { user ->
            UserDto(
                id = user.id!!,
                name = user.getName(),
                username = user.username,
                email = user.getEmail(),
                role = user.role.name,
                avatarUrl = user.avatarUrl,
                firstLogin = user.firstLogin,
                lastLogin = user.lastLogin,
                createdAt = user.createdAt,
                profile = user.profile?.let { profile ->
                    UserProfileDto(
                        followers = profile.followers,
                        following = profile.following,
                        dayStreak = profile.dayStreak,
                        totalXp = profile.totalXp,
                        currentLeague = profile.currentLeague,
                        topFinishes = profile.topFinishes,
                        name = user.getName(),
                        username = user.username,
                        joinDate = profile.joinDate,
                        courses = profile.courses,
                        avatarUrl = user.avatarUrl,
                        friends = profile.friends,
                        dailyGoalOption = profile.dailyGoal,
                        streakHistory = profile.streakHistory
                    )
                }
            )
        }
    }

    @AdminOnly
    @PostMapping("/create-admin")
    fun createAdmin(@RequestBody adminDto: CreateAdminDto): Map<String, Any> {
        if (userService.existsByName(adminDto.username)) {
            throw ApiException(400, "Username already exists")
        }

        if (userService.findByEmail(adminDto.email).isPresent) {
            throw ApiException(400, "Email already exists")
        }

        val adminUser = com.example.deepsea.entity.User(
            name = adminDto.name,
            username = adminDto.username,
            password = hashService.hashBcrypt(adminDto.password),
            email = adminDto.email,
            role = Role.ADMIN,
            avatarUrl = adminDto.avatarUrl
        )

        val savedUser = userService.save(adminUser)

        val profile = UserProfile(
            name = adminDto.name,
            username = adminDto.username,
            user = savedUser,
            followers = 0,
            following = 0,
            dayStreak = 0,
            totalXp = 0,
            currentLeague = "Admin",
            topFinishes = 0
        )
        userProfileService.saveUserProfile(profile)

        return mapOf(
            "message" to "Admin user created successfully",
            "adminId" to savedUser.id!!,
            "username" to savedUser.username
        )
    }

    // Helper method để validate admin
    private fun validateAdmin(token: String) {
        if (!tokenService.validateToken(token)) {
            throw ApiException(401, "Invalid or expired token")
        }

        val role = tokenService.getRoleFromToken(token)
        if (role != Role.ADMIN) {
            throw ApiException(403, "Access denied. Admin role required")
        }
    }
}