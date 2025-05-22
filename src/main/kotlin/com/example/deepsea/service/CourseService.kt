package com.example.deepsea.service

import com.example.deepsea.enums.LanguageOption
import com.example.deepsea.enums.PathOption
import com.example.deepsea.dto.UserCourseDto
import com.example.deepsea.entity.UserCourse
import com.example.deepsea.entity.UserCourseId
import com.example.deepsea.repository.UserCourseRepository
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val userCourseRepository: UserCourseRepository
) {
    fun saveOrUpdatePath(userId: Long, course: LanguageOption, path: PathOption) {
        val id = UserCourseId(userId, course)

        userCourseRepository.findById(id).let { existing ->
            if (existing.isPresent) {
                existing.get().path = path
                userCourseRepository.save(existing.get())
            } else {
                userCourseRepository.save(UserCourse(id, path))
            }
        }
    }

    fun getCoursesWithPaths(userId: Long): List<UserCourseDto> {
        return userCourseRepository.findByUserId(userId).map {
            UserCourseDto(
                language = it.id.course,
                path = it.path
            )
        }
    }
}