package com.watcherr.backend.services

import com.watcherr.backend.entities.User
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

class Subscriber(val id: Long) : SseEmitter(Long.MAX_VALUE)

@Service
class NotificationService {
    val subscribers: MutableSet<Subscriber> = hashSetOf()

    fun subscribe(subscriber: Subscriber): Subscriber {
        subscribers.removeIf{sub -> sub.id == subscriber.id}
        subscribers.add(subscriber)
        return subscriber
    }

    fun unsubscribe(subscriber: Subscriber) {
        subscribers.remove(subscriber)
    }

    fun followNotify(followerName: String, followed: Long) {
        subscribers.findLast { subscriber -> subscriber.id == followed }.let {
            it?.send("${followerName} followed you")
        }
    }

    fun likeNotify(liker: User, showName: String) {
        liker.followers?.forEach { follower ->
            subscribers.forEach { subscriber ->
                if (subscriber.id == follower.id) subscriber.send(
                    "${liker.name} liked ${showName}"
                )
            }
        }
    }
}