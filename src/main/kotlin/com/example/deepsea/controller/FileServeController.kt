package com.example.deepsea.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/api/uploads")
class FileServeController(
    @Value("\${app.upload.dir:uploads}")
    private val uploadDir: String
) {

    @GetMapping("/{fileName}")
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val path = Paths.get(uploadDir).resolve(fileName)
        val resource = UrlResource(path.toUri())

        // Check if file exists and is readable
        if (resource.exists() && resource.isReadable) {
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.filename + "\"")
                .contentType(MediaType.IMAGE_JPEG) // You might want to determine this dynamically
                .body(resource)
        }

        return ResponseEntity.notFound().build()
    }
}