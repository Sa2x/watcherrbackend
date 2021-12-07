package com.watcherr.backend.services

import com.watcherr.backend.dtos.ExternalEpisodeDTO
import com.watcherr.backend.dtos.ExternalShowDTO
import com.watcherr.backend.dtos.GetDescriptiveShowDTO
import com.watcherr.backend.dtos.GetShowForListDTO
import com.watcherr.backend.entities.Show
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}

@Service
class ExternalShowService(private val restTemplate: RestTemplate) {
    val BASE_URI = "https://api.tvmaze.com/shows"

    fun getAllShows(page: Long): List<GetShowForListDTO>? {
        val response = restTemplate.exchange(
            BASE_URI.plus("?page=").plus(page),
            HttpMethod.GET,
            null,
            typeRef<List<ExternalShowDTO>>()
        )
        return response.body?.map { showDTO ->
            GetShowForListDTO(
                apiId = showDTO.id,
                imgSrc = showDTO.image?.original.orEmpty(),
                name = showDTO.name,
                status = showDTO.status.orEmpty(),
                ratingAverage = showDTO.rating?.average
            )
        }
    }

    fun getShowById(id: Int): GetDescriptiveShowDTO {
        val show: ExternalShowDTO? =
            restTemplate.exchange(BASE_URI.plus("/").plus(id), HttpMethod.GET, null, typeRef<ExternalShowDTO>()).body
        val seasons: List<Any>? = restTemplate.exchange(
            BASE_URI.plus("/").plus(id).plus("/seasons"),
            HttpMethod.GET,
            null,
            typeRef<List<Any>>()
        ).body
        val episodes: List<ExternalEpisodeDTO>? = restTemplate.exchange(
            BASE_URI.plus("/").plus(id).plus("/episodes"),
            HttpMethod.GET,
            null,
            typeRef<List<ExternalEpisodeDTO>>()
        ).body
        return GetDescriptiveShowDTO(
            apiId = show!!.id,
            imgSrc = show.image?.original.orEmpty(),
            name = show.name,
            status = show.status.orEmpty(),
            ratingAverage = show.rating?.average,
            genres = show.genres!!,
            summary = show.summary!!,
            seasonCount = seasons!!.size,
            episodes = episodes!!
        )
    }
}