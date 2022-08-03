package org.du.minigames.app

import org.apache.http.HttpStatus
import org.du.minigames.app.dto.AppraiseMessageRequest
import org.du.minigames.app.dto.BaseResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.service.MessageService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/message")
class MessageResource(private val messageService: MessageService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getMessage(
        @QueryParam("tarnished") tarnishedUid: String?,
        @QueryParam("message") messageUid: String?): Response {
        if (!tarnishedUid.isNullOrBlank()) return Response.ok(messageService.getMessagesFromTarnished(tarnishedUid)).build()
        else if (!messageUid.isNullOrBlank()) return Response.ok(messageService.getMessageFromUid(messageUid)).build()
        else return Response.noContent().build()
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun sendMessage(request: SendMessageRequest) =
        Response.ok(BaseResponse(status = HttpStatus.SC_OK, message = messageService.sendMessage(request).name))
            .build()

    @Path("/appraise")
    @Produces(MediaType.APPLICATION_JSON)
    fun appraiseMessage(request: AppraiseMessageRequest) =
        Response.ok(BaseResponse(
            status = HttpStatus.SC_OK, message = messageService.queueAppraisePayload(request).name))
            .build()
}