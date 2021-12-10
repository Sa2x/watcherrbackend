package com.watcherr.backend.controllers

import com.watcherr.backend.auth.JwtUtils
import com.watcherr.backend.dtos.ExternalShowDTO
import com.watcherr.backend.dtos.GetDescriptiveShowDTO
import com.watcherr.backend.dtos.GetShowForListDTO
import com.watcherr.backend.dtos.Profile
import com.watcherr.backend.entities.Show
import com.watcherr.backend.services.ExternalShowService
import com.watcherr.backend.services.NotificationService
import com.watcherr.backend.services.ShowService
import com.watcherr.backend.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@CrossOrigin
@RequestMapping("/api/show")
class ShowController(private val externalShowService: ExternalShowService, private val userService: UserService, private val showService: ShowService, private val notificationService: NotificationService) {

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @GetMapping()
    fun getAllShows(@RequestParam page: Long): ResponseEntity<List<GetShowForListDTO>>{
        val showList = externalShowService.getAllShows(page)
        return ResponseEntity(showList, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getShowById(@PathVariable id: Int): ResponseEntity<GetDescriptiveShowDTO> = ResponseEntity(externalShowService.getShowById(id),HttpStatus.OK)

    @PostMapping("/{id}/like")
    fun likeShow(@RequestHeader("Authorization") token: String, @PathVariable id:Int):ResponseEntity<Any>{
        val user: Profile = jwtUtils.getUserFromJwt(token)
        val foundUser = userService.getUserById(user.id)
        val show: GetDescriptiveShowDTO = externalShowService.getShowById(id)
        val likedShows = foundUser.likedShows as MutableSet<Show>
        val foundShow = showService.getShowById(show.apiId)
        if(foundShow.isPresent){
            likedShows.add(foundShow.get())
            userService.saveUser(foundUser.copy(likedShows = likedShows))
        }
        else{
            val addedShow :Show = showService.saveShow(Show(name=show.name,apiId = show.apiId,ratingAverage = show.ratingAverage,imgSrc = show.imgSrc, status = show.status, null))
            likedShows.add(addedShow)
            userService.saveUser(foundUser.copy(likedShows = likedShows))
        }
        notificationService.likeNotify(foundUser,show.name)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{id}/unlike")
    fun unlikeShow(@RequestHeader("Authorization") token: String, @PathVariable id:Long):ResponseEntity<Any>{
        val user: Profile = jwtUtils.getUserFromJwt(token)
        val foundUser = userService.getUserById(user.id)
        val likedShows = foundUser.likedShows as MutableSet<Show>
        val foundShow = showService.getShowById(id)
        if(foundShow.isPresent){
            likedShows.remove(foundShow.get())
            userService.saveUser(foundUser.copy(likedShows = likedShows))
            return ResponseEntity.ok().build()
        }
        else{
            return ResponseEntity("Show with id not found", HttpStatus.BAD_REQUEST)
        }

    }

}