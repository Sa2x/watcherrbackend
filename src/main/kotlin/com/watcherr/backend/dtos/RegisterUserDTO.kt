package com.watcherr.backend.dtos

import org.springframework.web.multipart.MultipartFile

data class RegisterUserDTO(
    val userName: String,
    val password: String,
    val email: String,
    val profilePicture: MultipartFile?
)
