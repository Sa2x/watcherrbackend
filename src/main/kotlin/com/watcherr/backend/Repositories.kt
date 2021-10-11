package com.watcherr.backend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User,Long>{

}

interface ShowRepository : CrudRepository<Show, Long>{

}