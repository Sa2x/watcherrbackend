package com.watcherr.backend.auth

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomExceptionsHandler {
    @ExceptionHandler(AuthException::class)
    fun authenticationExceptionHandler(e: Exception): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
    }
}