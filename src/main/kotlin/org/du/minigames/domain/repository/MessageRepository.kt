package org.du.minigames.domain.repository

import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.enum.StatusEnum

interface MessageRepository {

    val messages: MutableList<MessageResponse>

    fun findMessageByUid(uid: String): MessageResponse?
    fun findMessageByTarnishedUid(uid: String): List<MessageResponse>
    fun createNewMessage(request: SendMessageRequest): StatusEnum
}