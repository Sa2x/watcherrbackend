package com.watcherr.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.watcherr.backend.entities.User
import javax.persistence.ElementCollection
import javax.persistence.Id
import javax.persistence.ManyToMany

data class GetDescriptiveShowDTO(
    val name:String,
    val apiId: Long,
    var ratingAverage: Double?,
    var imgSrc: String?,
    var status: String?,
    val genres:List<String>,
    val summary: String,
    val seasonCount: Int,
    val episodes:List<ExternalEpisodeDTO>
)
