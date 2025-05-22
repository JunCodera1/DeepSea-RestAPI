package com.example.deepsea.entity

import com.example.deepsea.enums.Role
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "api_user_id_seq")
    @SequenceGenerator(name = "api_user_id_seq", allocationSize = 1)
    val id: Long? = null,

    @Column(unique = true, name = "name")
    private var name: String = "",

    @Column(unique = true, name = "username")
    private var username: String = "",

    @Column(name = "password")
    private var password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: Role = Role.USER,

    @field:Email
    @Column(unique = true, nullable = false, name = "email")
    private var email: String = "",

    @Column(name = "avatar_url")
    var avatarUrl: String? = null,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    var profile: UserProfile? = null,

    @Column(name = "is_first_login")
    var firstLogin: Boolean = true,

    @Column(name = "last_login")
    var lastLogin: LocalDate? = null,

    @Column(name = "created_at")
    var createdAt: LocalDate = LocalDate.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDate = LocalDate.now(),

    @Column(name = "is_active")
    var isActive: Boolean? = true
) : UserDetails {

    // Implement UserDetails với proper authorities dựa trên role
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    }

    override fun getPassword(): String = password
    override fun getUsername(): String = username

    // Getter methods để access private fields
    fun getEmail(): String = email
    fun getName(): String = name

    // Setter methods để update fields
    fun updateName(newName: String) {
        this.name = newName
        this.updatedAt = LocalDate.now()
    }

    fun updateUsername(newUsername: String) {
        this.username = newUsername
        this.updatedAt = LocalDate.now()
    }

    fun updateEmail(newEmail: String) {
        this.email = newEmail
        this.updatedAt = LocalDate.now()
    }

    fun updatePassword(newPassword: String) {
        this.password = newPassword
        this.updatedAt = LocalDate.now()
    }

    fun updateRole(newRole: Role) {
        this.role = newRole
        this.updatedAt = LocalDate.now()
    }

    fun updateLastLogin() {
        this.lastLogin = LocalDate.now()
        this.updatedAt = LocalDate.now()
    }

    fun markFirstLoginComplete() {
        this.firstLogin = false
        this.updatedAt = LocalDate.now()
    }

    // Convenience methods
    fun isAdmin(): Boolean = role == Role.ADMIN
    fun isUser(): Boolean = role == Role.USER

    // Lifecycle callbacks
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDate.now()
    }
}