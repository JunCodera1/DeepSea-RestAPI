package com.example.deepsea.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toFormattedJoinDate(): String {
    val day = this.dayOfMonth
    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
    val formatter = DateTimeFormatter.ofPattern("MMMM")
    val month = this.format(formatter)
    val year = this.year
    return "$month $day$suffix, $year"
}
