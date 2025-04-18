package com.example.deepsea.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "api_user_id")
    @SequenceGenerator(name = "api_user_id_seq", allocationSize = 1)
    val id: Long? = 0,

    @Column(unique = true, name = "username")
    private val username: String = "",

    @Column(name = "password")
    private val password: String = "",

    @field:Email
    @Column(unique = true, nullable = false, name = "email")
    private val email: String = "",

    @Column(name = "avatar_url")
    private val avatarUrl: String? = null
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    // Use the property directly instead of implementing these methods
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    fun getEmail(): String = email

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}