package org.du.minigames.domain.repository

import org.du.minigames.app.dto.TarnishedResponse

interface TarnishedRepository {

    val tarnisheds: List<TarnishedResponse>

    fun allTarnished(): List<TarnishedResponse>
    fun findTarnishedByUid(uid: String): TarnishedResponse?
}