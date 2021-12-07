package com.watcherr.backend.controllers

import com.watcherr.backend.auth.Auth
import com.watcherr.backend.auth.JwtUtils
import com.watcherr.backend.dtos.*
import com.watcherr.backend.entities.User
import com.watcherr.backend.services.UserService
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException

@RestController
@CrossOrigin
@RequestMapping("/api/user")
class UserController(private val userService: UserService, private val passwordEncoder: BCryptPasswordEncoder) {

    @Autowired
    lateinit var jwtUtils:JwtUtils

    @GetMapping("/")
    fun findAll() = ResponseEntity.ok(userService.getAll())

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id:Long) = ResponseEntity.ok(userService.getUserById(id))

    @PostMapping("/register")
    fun register(@ModelAttribute user: RegisterUserDTO): ResponseEntity<Any> {
        try {
            userService.registerUser(user)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/login")
    fun register(@RequestBody user: LoginUserDTO): ResponseEntity<Any> {
        try {
            val jwt: String = userService.loginUser(user)
            return ResponseEntity.ok(TokenResponseDTO(jwt))
        } catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/reset")
    fun resetPassword(@RequestBody user: ForgottenPasswordDTO): ResponseEntity<Any> {
        try {
            userService.resetPasswordForUser(user)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/me")
    fun getCurrentUser(@RequestHeader("Authorization") token: String): ResponseEntity<Any> {
        val user: Profile = jwtUtils.getUserFromJwt(token)
        try {
            return ResponseEntity.ok(user)
        } catch (e: Exception) {
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/{id}/picture")
    fun getProfilePicture(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val image: ByteArray? = userService.getUserById(id).profilePicture

            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${System.currentTimeMillis()}\"")
                .body(image)

        } catch (error: NoSuchElementException) {
            ResponseEntity
                .notFound()
                .build()
        }

    }
}