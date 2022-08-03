package org.du.minigames.consumer

import com.azure.messaging.servicebus.ServiceBusClientBuilder
import com.azure.messaging.servicebus.ServiceBusErrorContext
import com.azure.messaging.servicebus.ServiceBusProcessorClient
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.logging.Log
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.enum.PayloadType
import org.du.minigames.service.MessageService
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes


@ApplicationScoped
class MessageConsumer(
    private val messageSerivce: MessageService
): Runnable {

    lateinit var client: ServiceBusProcessorClient

    @ConfigProperty(name = "azure.servicebus.connectionString")
    lateinit var connectionString: String
    @ConfigProperty(name = "azure.servicebus.topic")
    lateinit var topicName: String
    @ConfigProperty(name = "azure.servicebus.subscription")
    lateinit var subName: String

    private val scheduler = Executors.newSingleThreadExecutor()

    fun onStart(@Observes ev: StartupEvent?) {
        scheduler.submit(this)
    }

    fun onStop(@Observes ev: ShutdownEvent?) {
        scheduler.shutdown()
        Log.info("Stopping and closing the topic processor")
        client.close()
    }

    fun processMessage(context: ServiceBusReceivedMessageContext) {
        val message = context.message
        val objectMapper = ObjectMapper()
        Log.info("message received ${message.subject}:${message.messageId} sequence:#${message.sequenceNumber} payload:${message.body}")
        if (message.subject == PayloadType.MESSAGE.name) messageSerivce.createMessage(objectMapper.readValue(message.body.toString(), SendMessageRequest::class.java))
        else if (message.subject == PayloadType.APPRAISE.name) messageSerivce.appraiseMessage(objectMapper.readValue(message.body.toString(), AppraiseMessageRequest::class.java))
        context.complete()
    }
    fun processError(context: ServiceBusErrorContext?, countdownLatch: CountDownLatch) {
        Log.error("Error receving message:${context!!.errorSource}, countdown:${countdownLatch.count}")
    }

    override fun run() {
        if (!connectionString.isNullOrBlank() &&
            !topicName.isNullOrBlank() &&
            !subName.isNullOrBlank()) {
            val countdownLatch = CountDownLatch(1)

            // Create an instance of the processor through the ServiceBusClientBuilder
            client = ServiceBusClientBuilder()
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
            Log.info("Starting the topic processor")
            client.start()
        }
    }
}