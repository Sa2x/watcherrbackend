package com.watcherr.backend

import org.hibernate.mapping.Join
import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
data class User(
    val name:String,

    val email:String,

    @ManyToMany
    @JoinTable(
        name = "show_like",
        joinColumns = [JoinColumn(name = "user_id",referencedColumnName = "id" )],
        inverseJoinColumns = [JoinColumn(name="show_id",referencedColumnName = "apiId")])
    val likedShows: List<Show>,

    @Id @GeneratedValue
    var id: Long? = null)

@Entity
data class Show(

    val name:String,

    @Id
    val apiId: Long,

    @ManyToMany(mappedBy = "likedShows")
    val likes: List<User>,
)