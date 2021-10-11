package com.watcherr.backend

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository){

    @GetMapping("/")
    fun findAll() = userRepository.findAll()

    @PostMapping("/")
    fun createUser(@RequestBody user: User) = userRepository.save(user)
}