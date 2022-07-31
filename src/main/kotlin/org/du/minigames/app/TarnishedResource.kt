package org.du.minigames.app

import org.du.minigames.app.dto.TarnishedResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/tarnished")
class TarnishedResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun allTarnished() = ArrayList<TarnishedResponse>()

    @GET
    @Path("/{tarnishedGuid}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTarnishedByGuid (tarnishedGuid: String?) = TarnishedResponse()
}