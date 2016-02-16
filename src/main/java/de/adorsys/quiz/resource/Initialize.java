package de.adorsys.quiz.resource;

import de.adorsys.quiz.helper.GpioHelper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/init")
public class Initialize {

	@GET
	public Response reset(@Context UriInfo uriInfo) {
		GpioHelper.restart();

		return Response.ok().header("X-riddle", generateRiddleUrl(uriInfo, 0)).build();
	}

	private String generateRiddleUrl(UriInfo uriInfo, int id) {
		return uriInfo.getBaseUriBuilder().path("riddle").path(String.valueOf(id)).build().toString();
	}

	@GET
	@Path("/restart")
	public Response restart(@Context UriInfo uriInfo) {
		GpioHelper.restart();

		return Response.ok().header("X-riddle", generateRiddleUrl(uriInfo, 1)).build();
	}

}
