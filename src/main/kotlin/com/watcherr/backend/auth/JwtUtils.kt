package com.watcherr.backend.auth

import com.watcherr.backend.dtos.GetShowForListDTO
import com.watcherr.backend.dtos.Profile
import com.watcherr.backend.entities.User
import com.watcherr.backend.repositories.UserRepository
import com.watcherr.backend.services.UserService
import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
class JwtUtils(private val userService: UserService) {
    fun getUserFromJwt(token: String): Profile {
        val authToken = token.substring(7)
        val claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(authToken).body
        val user: User = userService.getUserById((claims["id"] as String).toLong())
        var imageUrl = ""
        if (user.profilePicture != null) {
            imageUrl = "http://localhost:8080/api/user/${user.id}/picture"
        }
        return Profile(user.id!!, user.name,
            user.likedShows?.map { show ->
                GetShowForListDTO(
                    apiId = show.apiId,
                    imgSrc = show.imgSrc,
                    name = show.name,
                    status = show.status,
                    ratingAverage = show.ratingAverage
                )
            }, imageUrl = imageUrl
        )
    }
}