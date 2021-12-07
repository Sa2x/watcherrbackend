package com.watcherr.backend.repositories

import com.watcherr.backend.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository : JpaRepository<User, Long> {

    fun existsByEmail(email: String):Boolean
    fun findByEmail(email: String): User
}