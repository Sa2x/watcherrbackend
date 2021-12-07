package com.watcherr.backend.dtos

import com.watcherr.backend.entities.Show

data class Profile(
    val id: Long,
    val name: String,
    val likedShows: List<GetShowForListDTO>?,
    val imageUrl: String?
)
