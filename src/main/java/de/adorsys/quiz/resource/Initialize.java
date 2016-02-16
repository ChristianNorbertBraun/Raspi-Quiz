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

		return Response.ok().header("X-riddle", generateRiddleUrl(uriInfo)).build();
	}

	private String generateRiddleUrl(UriInfo uriInfo) {
		return uriInfo.getBaseUriBuilder().path("riddle").path("0").build().toString();
	}

}
