package de.adorsys.quiz.resource;

import de.adorsys.quiz.entity.Answer;
import de.adorsys.quiz.entity.Riddle;
import de.adorsys.quiz.helper.FileManager;
import de.adorsys.quiz.helper.GpioHelper;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/riddle/{riddleId}")
public class Quiz {

	private FileManager fileManager;

	@PostConstruct
	public void init() {
		GpioHelper.start();
		fileManager = FileManager.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRiddle(@PathParam("riddleId") int riddleId, @Context UriInfo uriInfo) {
		Riddle riddle = fileManager.readRiddle(riddleId);
		if (riddle == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		return Response.ok(riddle).header("X-answer", generateAnswerUrl(uriInfo, riddle.getId())).build();
	}

	@PUT
	public Response answerRiddle(@PathParam("riddleId") int riddleId,  @Context UriInfo uriInfo, Answer answer) {
		Riddle riddle = fileManager.readRiddle(riddleId);
		if (!answer.getAnswer().toLowerCase().equals(riddle.getAnswer().toLowerCase()))
			return Response.status(Response.Status.BAD_REQUEST).build();
		GpioHelper.shutLED(riddle.getId());
		if (riddleId < 5)
			return Response.ok().header("X-riddle", generateRiddleUrl(uriInfo, riddleId)).build();
		return Response.noContent().build();
	}

	private String generateAnswerUrl(UriInfo uriInfo, int id) {
		return uriInfo.getBaseUriBuilder().path("riddle").path(String.valueOf(id)).build().toString();
	}

	private String generateRiddleUrl(UriInfo uriInfo, int id) {
		return uriInfo.getBaseUriBuilder().path("riddle").path(String.valueOf(id)).build().toString();
	}

}
