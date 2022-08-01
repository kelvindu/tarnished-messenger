package org.du.minigames.app.dto


import com.fasterxml.jackson.annotation.JsonProperty
/**
 * 
 * @param tarnishedUid 
 * @param subject 
 * @param subjectTemplate 
 */

data class SendMessageRequest (
    @field:JsonProperty("request_id")
    var requestId: String? = null,
    @field:JsonProperty("tarnished_uid")
    var tarnishedUid: String? = null,
    @field:JsonProperty("subject")
    var subject: String? = null,
    @field:JsonProperty("subject_template")
    var subjectTemplate: Int? = null
)

