package com.example.deepsea.repository

import com.example.deepsea.dto.LanguageOption
import com.example.deepsea.dto.PathOption
import com.example.deepsea.model.UserCourse
import com.example.deepsea.model.UserCourseId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserCourseRepository : JpaRepository<UserCourse, UserCourseId> {

    @Query("SELECT uc FROM UserCourse uc WHERE uc.id.userProfileId = :userId")
    fun findByUserId(userId: Long): List<UserCourse>

    @Modifying
    @Query("UPDATE UserCourse uc SET uc.path = :path WHERE uc.id.userProfileId = :userId AND uc.id.course = :course")
    fun updatePath(
        @Param("userId") userId: Long,
        @Param("course") course: LanguageOption,
        @Param("path") path: PathOption
    )
}