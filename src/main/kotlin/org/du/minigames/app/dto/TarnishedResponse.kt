package org.du.minigames.app.dto


import com.fasterxml.jackson.annotation.JsonProperty
/**
 * 
 * @param uid 
 * @param username 
 * @param status 
 */

data class TarnishedResponse (
    @field:JsonProperty("uid")
    var uid: String? = null,
    @field:JsonProperty("username")
    var username: String? = null,
    @field:JsonProperty("status")
    var status: String? = null
)

