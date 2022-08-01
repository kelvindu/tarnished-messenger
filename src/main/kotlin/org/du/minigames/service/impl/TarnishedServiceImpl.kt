package org.du.minigames.service.impl

import org.du.minigames.domain.repository.stub.TarnishedRepositoryStub
import org.du.minigames.service.TarnishedService
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TarnishedServiceImpl(val tarnishedRepository: TarnishedRepositoryStub): TarnishedService {

    override fun allTarnished() = tarnishedRepository.allTarnished()

    override fun findTarnished(uid: String) = tarnishedRepository.findTarnishedByUid(uid)!!
}