package org.du.minigames.app

import org.du.minigames.app.dto.TarnishedResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/tarnished")
class TarnishedResource(val tarnishedServiceImpl: TarnishedService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun allTarnished() = tarnishedServiceImpl.allTarnished()

    @GET
    @Path("/{tarnishedGuid}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTarnishedByGuid (tarnishedGuid: String?) = tarnishedServiceImpl.findTarnished(tarnishedGuid!!)
}