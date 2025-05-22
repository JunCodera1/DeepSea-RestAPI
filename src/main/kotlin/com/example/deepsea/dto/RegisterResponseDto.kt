import jakarta.validation.constraints.Email

data class RegisterResponseDto(
    val id: Long?,
    val name:String,
    val username: String,
    val password: String,
    @field:Email(message = "Email is not validation")
    val email: String,
    val avatarUrl: String? = null,
    val role: String? = null
)