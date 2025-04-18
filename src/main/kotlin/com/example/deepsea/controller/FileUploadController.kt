package com.example.deepsea.controller

import com.example.deepsea.dto.UploadResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/api/upload")
class FileUploadController(
    @Value("\${app.upload.dir:uploads}")
    private val uploadDir: String
) {

    @PostMapping("/avatar", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadAvatar(@RequestPart("avatar") file: MultipartFile): ResponseEntity<UploadResponseDto> {
        val fileName = UUID.randomUUID().toString() + getExtension(file.originalFilename ?: "")
        val path = Paths.get(uploadDir)

        // Create directory if it doesn't exist
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }

        // Save the file
        val filePath = path.resolve(fileName)
        Files.copy(file.inputStream, filePath)

        // Create a URL for client access
        val fileUrl = "/api/uploads/$fileName"

        return ResponseEntity.ok(UploadResponseDto(
            success = true,
            url = fileUrl,
            message = "File uploaded successfully"
        ))
    }

    private fun getExtension(filename: String): String {
        return if (filename.contains(".")) {
            filename.substring(filename.lastIndexOf("."))
        } else {
            ""
        }
    }
}