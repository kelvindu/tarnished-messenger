package org.du.minigames.di

import com.azure.messaging.servicebus.ServiceBusClientBuilder
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.inject.Singleton
import javax.ws.rs.Produces

@Singleton
class AzureServiceBus {
    @ConfigProperty(name = "azure.servicebus.connectionString")
    lateinit var connectionString: String
    @ConfigProperty(name = "azure.servicebus.topic")
    lateinit var topicName: String


    @Produces
    @ApplicationScoped
    fun initSenderClient() = ServiceBusClientBuilder()
        .connectionString(connectionString)
        .sender()
        .topicName(topicName)
        .buildClient()
}

