package org.du.minigames.domain.repository.stub

import org.du.minigames.app.dto.TarnishedResponse
import org.du.minigames.domain.repository.TarnishedRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TarnishedRepositoryStub: TarnishedRepository {

    override val tarnisheds: List<TarnishedResponse> = arrayListOf(
        TarnishedResponse(uid = "0f2c46a9-fbdf-47bc-bebb-939bf5618501", username = "letmesoloher", status = "alive"),
        TarnishedResponse(uid = "0f2c46a9-ffdd-47bb-bebb-939bf5618501", username = "letmesolome", status = "dead"),
        TarnishedResponse(uid = "0f2c46a9-55dd-47bb-bebb-939bf5612222", username = "wretchsolo", status = "alive"),
        TarnishedResponse(uid = "0f2c46a9-ffdd-aabb-dddd-939bf5618501", username = "maidenlessboi", status = "alive")
    )

    override fun allTarnished(): List<TarnishedResponse> = tarnisheds

    override fun findTarnishedByUid(uid: String): TarnishedResponse? {
        for (tarnished in tarnisheds)
            if (tarnished.uid == uid) return tarnished
        return null
    }
}