package org.du.minigames.service.impl

import com.azure.messaging.servicebus.ServiceBusMessage
import com.azure.messaging.servicebus.ServiceBusSenderClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.domain.repository.MessageRepository
import org.du.minigames.enum.PayloadType
import org.du.minigames.enum.StatusEnum
import org.du.minigames.service.MessageService
import org.du.minigames.service.TarnishedService
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class MessageServiceImpl(
    private val client: ServiceBusSenderClient,
    private val tarnishedService: TarnishedService,
    private val messageRespository: MessageRepository
): MessageService {

    override fun getMessageFromUid(messageUid: String): MessageResponse? = messageRespository.findMessageByUid(messageUid)

    override fun getMessagesFromTarnished(tarnishedUid: String): List<MessageResponse> = messageRespository.findMessageByTarnishedUid(tarnishedUid)

    override fun sendMessage(request: SendMessageRequest): StatusEnum {
        val objectMapper = ObjectMapper()
        try {
            val newMessage = ServiceBusMessage(objectMapper.writeValueAsString(request))
            newMessage.subject = PayloadType.MESSAGE.name
            client.sendMessage(newMessage)
            return StatusEnum.CREATED
        } catch (e: Exception) {
            return StatusEnum.FAILED
        }
    }

    override fun createMessage(request: SendMessageRequest) {
        if (request.tarnishedUid != null)
            messageRespository.createNewMessage(request)
        else
            throw Exception("User not found")
    }

    override fun queueAppraisePayload(request: AppraiseMessageRequest): StatusEnum {
        val objectMapper = ObjectMapper()
        try {
            val newMessage = ServiceBusMessage(objectMapper.writeValueAsString(request))
            newMessage.subject = PayloadType.APPRAISE.name
            client.sendMessage(newMessage)
            return StatusEnum.CREATED
        } catch (e: Exception) {
            return StatusEnum.FAILED
        }
    }

    override fun appraiseMessage(request: AppraiseMessageRequest) {
        val message = messageRespository.findMessageByUid(request.messageUid!!)
        if (message != null) {
            messageRespository.appraiseMessage(message.uid!!, request)
        } else throw Exception("Message not found id:${request.requestId}")
    }
}