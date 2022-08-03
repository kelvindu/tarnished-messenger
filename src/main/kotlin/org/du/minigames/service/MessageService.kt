package org.du.minigames.service

import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.enum.StatusEnum

interface MessageService {
    fun getMessageFromUid(messageUid: String): MessageResponse?
    fun getMessagesFromTarnished(tarnishedUid: String): List<MessageResponse>
    fun sendMessage(request: SendMessageRequest): StatusEnum
    @Throws(Exception::class)
    fun createMessage(request: SendMessageRequest)
    fun queueAppraisePayload(request: AppraiseMessageRequest): StatusEnum
    @Throws(Exception::class)
    fun appraiseMessage(request: AppraiseMessageRequest)
}