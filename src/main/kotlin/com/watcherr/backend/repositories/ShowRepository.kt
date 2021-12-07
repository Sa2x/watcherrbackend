package com.watcherr.backend.repositories

import com.watcherr.backend.entities.Show
import org.springframework.data.repository.CrudRepository

interface ShowRepository : CrudRepository<Show, Long> {

}