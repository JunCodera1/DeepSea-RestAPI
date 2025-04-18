import jakarta.validation.constraints.Email

data class RegisterDto(
    val username: String,
    val password: String,
    @field:Email(message = "Email is not validation")
    val email: String,
    val avatarUrl: String? = null
)