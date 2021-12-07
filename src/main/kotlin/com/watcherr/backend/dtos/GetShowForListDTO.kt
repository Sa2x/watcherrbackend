package com.watcherr.backend.dtos

data class GetShowForListDTO(
    val apiId:Long,
    val imgSrc: String?,
    val name: String,
    val status: String?,
    val ratingAverage: Double?
)
