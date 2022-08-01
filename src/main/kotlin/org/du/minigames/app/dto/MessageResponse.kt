package org.du.minigames.app.dto


import com.fasterxml.jackson.annotation.JsonProperty
/**
 * 
 * @param uid 
 * @param tarnishedUid 
 * @param message 
 * @param goodRating 
 * @param badRating 
 */

data class MessageResponse (
    @field:JsonProperty("uid")
    var uid: String? = null,
    @field:JsonProperty("tarnished_uid")
    var tarnishedUid: String? = null,
    @field:JsonProperty("message")
    var message: String? = null,
    @field:JsonProperty("good_rating")
    var goodRating: Int? = null,
    @field:JsonProperty("bad_rating")
    var badRating: Int? = null
)

