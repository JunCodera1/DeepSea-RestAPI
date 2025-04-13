package com.example.deepsea.exception

class ApiException(
    val statusCode: Int,
    override val message: String
) : RuntimeException(message)
