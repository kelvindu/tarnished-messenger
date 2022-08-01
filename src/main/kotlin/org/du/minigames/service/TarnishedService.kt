package org.du.minigames.service

import org.du.minigames.app.dto.TarnishedResponse

interface TarnishedService {

    fun allTarnished(): List<TarnishedResponse>
    fun findTarnished(uid: String): TarnishedResponse
}