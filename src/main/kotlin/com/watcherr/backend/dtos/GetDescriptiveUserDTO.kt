package com.watcherr.backend.dtos

data class GetDescriptiveUserDTO(
    val id: Long,
    val name: String,
    val likedShows: List<GetShowForListDTO>?,
    val followed: List<Profile>?,
    val followers: List<Profile>?,
    val imageUrl: String?
)
