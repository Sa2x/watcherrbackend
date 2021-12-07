package com.watcherr.backend.services

import com.watcherr.backend.entities.Show
import com.watcherr.backend.repositories.ShowRepository
import org.springframework.stereotype.Service

@Service
class ShowService(private val showRepository: ShowRepository) {

    fun getShowById(id: Long) = showRepository.findById(id)
    fun saveShow(show:Show): Show = showRepository.save(show)
}