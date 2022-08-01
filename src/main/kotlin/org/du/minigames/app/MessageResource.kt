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
class MessageResource(val messageService: MessageService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getMessage(
        @QueryParam("tarnished") tarnishedUid: String?,
        @QueryParam("message") messageUid: String?) =
        Response.ok(messageService.getMessage(tarnishedUid!!, messageUid!!))
            .build()

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun sendMessage(request: SendMessageRequest) =
        Response.ok(BaseResponse(status = HttpStatus.SC_OK, message = messageService.sendMessage(request).name))
            .build()

    @Path("/appraise")
    @Produces(MediaType.APPLICATION_JSON)
    fun appraiseMessage(request: AppraiseMessageRequest) =
        Response.ok(BaseResponse(
            status = HttpStatus.SC_OK, message = messageService.appraiseMessage(request).name))
            .build()
}