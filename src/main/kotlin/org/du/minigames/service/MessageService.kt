package org.du.minigames.service

import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.enum.StatusEnum

interface MessageService {
    fun getMessage(tarnishedUid: String, messageUid: String): List<MessageResponse>
    fun sendMessage(request: SendMessageRequest): StatusEnum
    fun appraiseMessage(request: AppraiseMessageRequest): StatusEnum
}