package org.du.minigames.domain.repository.stub

import org.du.minigames.app.dto.MessageResponse
import org.du.minigames.app.dto.SendMessageRequest
import org.du.minigames.domain.repository.MessageRepository
import org.du.minigames.enum.StatusEnum
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MessageRepositoryStub: MessageRepository {

    fun generateMessage(subjectTemplate: Int, subject: String): String {
        val templates = arrayOf(
            "Praise the $subject",
            "Try $subject",
            "$subject ahead",
            "No $subject ahead",
            "$subject, O $subject",
            "Still no $subject...",
            "Could this be a $subject?",
            "Let there be $subject",
            "$subject required ahead",
            "Be wary of $subject",
            "Offer $subject",
            "Likely $subject",
            "If only I had a $subject...",
            "Ahh, $subject...",
            "First off, $subject",
            "Behold, $subject!",
            "Didn't expect $subject...",
            "Seek $subject",
            "Visions of $subject...",
            "Why is it always $subject?",
            "Time for $subject",
            "$subject",
            "$subject!",
            "$subject?",
            "$subject...",
        )
        return templates[subjectTemplate]
    }

    override val messages: MutableList<MessageResponse> = arrayListOf()

    override fun findMessageByUid(uid: String): MessageResponse? {
        for (message in messages)
            if (message.uid == uid) return message
        return null
    }

    override fun findMessageByTarnishedUid(uid: String): List<MessageResponse> {
        val tarnishedMessages = arrayListOf<MessageResponse>()
        for (message in messages)
            if (message.tarnishedUid == uid) tarnishedMessages.add(message)
        return tarnishedMessages
    }

    override fun createNewMessage(request: SendMessageRequest): StatusEnum {
        val newMessage = MessageResponse(
            uid = UUID.randomUUID().toString(),
            tarnishedUid = request.tarnishedUid,
            message = String.format(generateMessage(request.subjectTemplate!!, request.subject!!)),
            goodRating = 0,
            badRating = 0
        )
        messages.add(newMessage)
        return StatusEnum.SUCCESS
    }
}