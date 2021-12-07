package com.watcherr.backend.dtos

import javax.persistence.ElementCollection

data class ExternalShowDTO(
    val name:String,
    val id: Long,
    var rating : Rating?,
    var image: Image?,
    var status: String?,
    val genres:List<String>?,
    val summary: String?,
)

data class Rating(
    val average:Double?
)

data class Image(
    val original: String?
)
