package com.watcherr.backend.services

import com.watcherr.backend.dtos.*
import com.watcherr.backend.entities.User
import com.watcherr.backend.repositories.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*

@Service
class UserService(private val passwordEncoder: BCryptPasswordEncoder, private val userRepository: UserRepository) {

    fun registerUser(user: RegisterUserDTO) {
        if (userRepository.existsByEmail(user.email)) {
            throw Exception("User already exists")
        }
        userRepository.save(
            User(
                name = user.userName,
                email = user.email,
                password = passwordEncoder.encode(user.password),
                profilePicture = user.profilePicture?.bytes
            )
        )
    }

    fun getAll(): List<Profile> = userRepository.findAll().map { user ->
        var imageUrl = ""
        if (user.profilePicture != null) {
            imageUrl = "http://localhost:8080/api/user/${user.id}/picture"
        }
        Profile(
            name = user.name,
            id = user.id!!,
            likedShows = null,
            imageUrl = imageUrl,
            followed=null,
        )
    }

    fun loginUser(user: LoginUserDTO): String {
        if (!userRepository.existsByEmail(user.email)) {
            throw Exception("User with e-mail doesnt exist")
        }
        val foundUser: User = userRepository.findByEmail(user.email)
        if (!passwordEncoder.matches(user.password, foundUser.password)) {
            throw Exception("Password is wrong")
        }

        val claims: Map<String, Any> = hashMapOf(
            "id" to foundUser.id.toString(),
        )
        val jwt = Jwts.builder()
            .addClaims(claims)
            .setIssuer("backend")
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS512, "secret")
            .compact()

        return jwt
    }


    fun resetPasswordForUser(user: ForgottenPasswordDTO) {
        if (!userRepository.existsByEmail(user.email)) {
            throw Exception("User with e-mail doesnt exist")
        }
        val foundUser: User = userRepository.findByEmail(user.email)
        if (!passwordEncoder.matches(foundUser.password, user.password)) {
            throw Exception("Password is wrong")
        }

        userRepository.save(foundUser.copy(password = passwordEncoder.encode(user.newPassword)))
    }

    fun getUserById(id: Long):User{
        if (!userRepository.existsById(id)) {
            throw Exception("User doesn't exist with given id")
        }
        return userRepository.findById(id).get()
    }

    fun getProfileById(id: Long): GetDescriptiveUserDTO {
        if (!userRepository.existsById(id)) {
            throw Exception("User doesn't exist with given id")
        }
        val user: User = userRepository.findById(id).get()
        var imageUrl = ""
        if (user.profilePicture != null) {
            imageUrl = "http://localhost:8080/api/user/${user.id}/picture"
        }
        return GetDescriptiveUserDTO(
            name = user.name,
            id = user.id!!,
            imageUrl = imageUrl,
            likedShows = user.likedShows?.map { show ->
                GetShowForListDTO(
                    apiId = show.apiId,
                    imgSrc = show.imgSrc,
                    name = show.name,
                    status = show.status,
                    ratingAverage = show.ratingAverage
                )

            },
            followed=user.followedUsers?.map{followed->
                var image = ""
                if (followed.profilePicture != null) {
                    image = "http://localhost:8080/api/user/${followed.id}/picture"
                }
                Profile(
                    name = followed.name,
                    id = followed.id!!,
                    likedShows = null,
                    imageUrl = image,
                    followed = null
                )
            },
            followers = user.followers?.map { follower ->
                var img = ""
                if (follower.profilePicture != null) {
                    img = "http://localhost:8080/api/user/${follower.id}/picture"
                }
                Profile(
                    name = follower.name,
                    id = follower.id!!,
                    likedShows = null,
                    imageUrl = img,
                    followed = null
                )
            }
        )
    }

    fun saveUser(user: User): User = userRepository.save(user)

    fun existsById(id: Long): Boolean = userRepository.existsById(id)

}