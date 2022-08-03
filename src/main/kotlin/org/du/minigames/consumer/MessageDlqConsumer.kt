package org.du.minigames.consumer

import com.azure.messaging.servicebus.*
import io.quarkus.logging.Log
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class MessageDlqConsumer(
    private val senderClient: ServiceBusSenderClient
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
        Log.info("DLQ message is ${message.subject}:${message.messageId}:${message.body}")
        val reMessage = ServiceBusMessage(message.body)
        reMessage.subject = message.subject
        reMessage.messageId = message.messageId
        senderClient.sendMessage(reMessage)
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
                .subscriptionName(subName + "/\$deadletterqueue")
                .processMessage(this::processMessage)
                .processError { context: ServiceBusErrorContext? ->
                    processError(
                        context,
                        countdownLatch
                    )
                }
                .buildProcessorClient()
            Log.info("Starting the topic DLQ processor")
            client.start()
        }
    }
}