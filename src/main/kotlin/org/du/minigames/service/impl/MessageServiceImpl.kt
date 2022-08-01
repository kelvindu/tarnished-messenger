package org.du.minigames.service.impl

import com.azure.messaging.servicebus.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.du.minigames.app.dto.AppraiseMessageRequest
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
    var connectionString: String? = null
    @ConfigProperty(name = "azure.servicebus.topic")
    var topicName: String? = null
    @ConfigProperty(name = "azure.servicebus.subscription")
    var subName: String? = null

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

    // handles received messages
    @Throws(InterruptedException::class)

    fun receiveMessages() {
        val countdownLatch = CountDownLatch(1)

        // Create an instance of the processor through the ServiceBusClientBuilder
        val processorClient = ServiceBusClientBuilder()
            .connectionString(connectionString)
            .processor()
            .topicName(topicName)
            .subscriptionName(subName)
            .processMessage(this::processMessage)
            .processError { context: ServiceBusErrorContext? ->
                processError(
                    context,
                    countdownLatch
                )
            }
            .buildProcessorClient()
        println("Starting the processor")
        processorClient.start()
        TimeUnit.SECONDS.sleep(10)
        println("Stopping and closing the processor")
        processorClient.close()
    }

    fun processMessage(context: ServiceBusReceivedMessageContext ) {
    }
    fun processError(context: ServiceBusErrorContext?, countdownLatch: CountDownLatch) {
    }

    override fun appraiseMessage(request: AppraiseMessageRequest) = StatusEnum.CREATED
}