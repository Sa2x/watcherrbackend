package com.watcherr.backend.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.jayway.jsonpath.internal.function.numeric.Average
import jdk.jfr.DataAmount
import javax.persistence.*

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
data class Show(

    val name:String,

    @Id
    @JsonProperty("id")
    val apiId: Long,

    var ratingAverage: Double?,

    var imgSrc: String?,

    var status: String?,

    @ManyToMany(mappedBy = "likedShows")
    val likes: List<User>?,

    )