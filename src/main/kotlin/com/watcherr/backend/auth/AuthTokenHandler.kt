package com.watcherr.backend.auth

import com.watcherr.backend.entities.User
import com.watcherr.backend.repositories.UserRepository
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthTokenHandler(private val userRepository: UserRepository) {

    @Transactional
    fun getUserFromToken(token: String?): User? {
        if (token == null)
            throw AuthException("JWT Token not found")
        val claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).body
        if(!userRepository.existsById(claims["id"] as Long)){
            throw AuthException("Authentication failed, JWT is not valid")
        }
        return userRepository.findById(claims["id"] as Long).orElse(null)
    }

}