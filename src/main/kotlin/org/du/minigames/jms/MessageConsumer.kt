package org.du.minigames.jms

import com.azure.messaging.servicebus.ServiceBusClientBuilder
import com.azure.messaging.servicebus.ServiceBusErrorContext
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.service.MessageService
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.lang.RuntimeException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.jms.*

@ApplicationScoped
class MessageConsumer: Runnable {
//    @Inject
//    @field:Default
//    lateinit var connectionFactory: ConnectionFactory

    @Inject
    @field:Default
    lateinit var messageService: MessageService

    private val scheduler: ExecutorService = Executors.newSingleThreadExecutor()

    @Volatile
    private var lastPrice: String? = null

//    @ConfigProperty(name = "topic.tarnished-messenger")
//    lateinit var topicName: String

    fun onStart(@Observes ev: StartupEvent?) {
        scheduler.submit(this)
    }

    fun onStop(@Observes ev: ShutdownEvent?) {
        scheduler.shutdown()
    }

    override fun run() {
//        try {
//            connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE).use { context ->
//                val topic: Destination = context.createTopic(topicName)
//                val consumer: JMSConsumer = context.createConsumer(topic)
//                while (true) {
//                    val message: Message = consumer.receive()
//                    if (message == null) return
//                    val request = message.getBody(SendMessageRequest::class.java)
//                    messageService.createMessage(request)
//                }
//            }
//        } catch (e: JMSException) {
//            throw RuntimeException(e)
//        }
        try {
            receiveMessages()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    @ConfigProperty(name = "azure.servicebus.connectionString")
    lateinit var connectionString: String
    @ConfigProperty(name = "azure.servicebus.topic")
    lateinit var topicName: String
    @ConfigProperty(name = "azure.servicebus.subscription")
    lateinit var subName: String

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

    fun processMessage(context: ServiceBusReceivedMessageContext) {
        println(context.message)
    }
    fun processError(context: ServiceBusErrorContext?, countdownLatch: CountDownLatch) {
    }
}