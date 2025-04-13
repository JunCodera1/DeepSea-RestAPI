package com.example.deepsea.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "api_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "api_user_id")
    @SequenceGenerator(name = "api_user_id_seq", allocationSize = 1)
    val id: Long? = 0,

    @Column(unique = true)
    private val username: String = "",

    @Column
    private val password: String = ""
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    // Use the property directly instead of implementing these methods
    override fun getPassword(): String = password
    override fun getUsername(): String = username

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}