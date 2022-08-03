package org.du.minigames.app.dto


import com.fasterxml.jackson.annotation.JsonProperty
/**
 * 
 * @param tarnishedUid 
 * @param messageUid 
 * @param rating 
 */

data class AppraiseMessageRequest (
    @field:JsonProperty("request_id")
    var requestId: String? = null,
    @field:JsonProperty("tarnished_uid")
    var tarnishedUid: String? = null,
    @field:JsonProperty("message_uid")
    var messageUid: String? = null,
    @field:JsonProperty("rating")
    var rating: Int? = null
)

