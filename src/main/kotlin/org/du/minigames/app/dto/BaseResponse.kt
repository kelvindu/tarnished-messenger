package org.du.minigames.app.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BaseResponse (
    @field:JsonProperty var status: Int,
    @field:JsonProperty var message: String
)