package com.watcherr.backend.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*

@Entity
data class User(
    @Id @GeneratedValue
    var id: Long? = null,
    val name:String,
    val email:String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String?,
    @Lob
    val profilePicture: ByteArray? = byteArrayOf(),
    @ManyToMany
    @JoinTable(
        name = "show_like",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name="show_id")])
    @Fetch(FetchMode.JOIN)
    val likedShows: Set<Show>?= emptySet(),
    @ManyToMany
    @JoinTable(
        name = "user_follows",
        joinColumns = [ JoinColumn(name = "follower") ],
        inverseJoinColumns = [ JoinColumn(name = "followed") ]
    )
    @Fetch(FetchMode.JOIN)
    val followedUsers:Set<User>?= emptySet(),
    @ManyToMany(mappedBy = "followedUsers")
    val followers:Set<User>?= emptySet(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (profilePicture != null) {
            if (other.profilePicture == null) return false
            if (!profilePicture.contentEquals(other.profilePicture)) return false
        } else if (other.profilePicture != null) return false
        if (likedShows != other.likedShows) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (profilePicture?.contentHashCode() ?: 0)
        result = 31 * result + (likedShows?.hashCode() ?: 0)
        return result
    }
}