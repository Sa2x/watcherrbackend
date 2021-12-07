package com.watcherr.backend.dtos

data class ExternalEpisodeDTO(
    val season:Int,
    val number: Number,
    val name:String?,
    val summary:String?
)
