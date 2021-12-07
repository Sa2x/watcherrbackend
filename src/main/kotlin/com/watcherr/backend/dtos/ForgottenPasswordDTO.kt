package com.watcherr.backend.dtos

data class ForgottenPasswordDTO(
    val email:String,
    val password:String,
    val newPassword:String
)
