package org.du.minigames.domain.repository

import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest

interface MessageRepository {

    val messages: MutableList<MessageResponse>

    fun findMessageByUid(uid: String): MessageResponse?
    fun findMessageByTarnishedUid(uid: String): List<MessageResponse>
    fun createNewMessage(request: SendMessageRequest)
    fun appraiseMessage(uid:String, request: AppraiseMessageRequest)
}