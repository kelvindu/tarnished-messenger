package org.du.minigames.service.impl

import com.azure.messaging.servicebus.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.domain.repository.MessageRepository
import org.du.minigames.enum.StatusEnum
import org.du.minigames.service.MessageService
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class MessageServiceImpl(val messageRespository: MessageRepository): MessageService {

    @ConfigProperty(name = "azure.servicebus.connectionString")
    lateinit var connectionString: String
    @ConfigProperty(name = "azure.servicebus.topic")
    lateinit var topicName: String
    @ConfigProperty(name = "azure.servicebus.subscription")
    lateinit var subName: String

    fun initClient(): ServiceBusSenderClient = ServiceBusClientBuilder()
        .connectionString(connectionString)
        .sender()
        .topicName(topicName)
        .buildClient()

    override fun getMessage(tarnishedUid: String, messageUid: String) =
        if (tarnishedUid.isEmpty()) listOf(messageRespository.findMessageByUid(messageUid)!!)
        else messageRespository.findMessageByTarnishedUid(tarnishedUid)

    override fun sendMessage(request: SendMessageRequest): StatusEnum {
        val senderClient = initClient()
        val objectMapper = ObjectMapper()
        try {
            senderClient.sendMessage(ServiceBusMessage(objectMapper.writeValueAsString(request)))
            senderClient.close()
            return StatusEnum.CREATED
        } catch (e: Exception) {
            return StatusEnum.FAILED
        }
    }

    override fun createMessage(request: SendMessageRequest) {
        messageRespository.createNewMessage(request)
    }



    override fun appraiseMessage(request: AppraiseMessageRequest) = StatusEnum.CREATED
}