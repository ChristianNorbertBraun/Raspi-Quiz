package de.adorsys.quiz.resource;

import de.adorsys.quiz.entity.Answer;
import de.adorsys.quiz.entity.Riddle;
import de.adorsys.quiz.entity.Setting;
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

@Path("/riddle")
public class Quiz {

	private FileManager fileManager;

	@PostConstruct
	public void init() {
		GpioHelper.start();
		fileManager = FileManager.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRiddle(@Context UriInfo uriInfo) {
		Riddle riddle = chooseRiddle();
		if (riddle == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		return Response.ok(riddle).header("X-answer", generateAnswerUrl(uriInfo,  riddle.getId())).build();
	}

	@PUT
	@Path("/{riddleId}")
	public Response answerRiddle(@PathParam("riddleId") int riddleId,  @Context UriInfo uriInfo, Answer answer) {
		Riddle riddle = fileManager.readRiddle(riddleId);
		if (!answer.getAnswer().toLowerCase().equals(riddle.getAnswer().toLowerCase()))
			return Response.status(Response.Status.BAD_REQUEST).build();
		upgradeSettings();
		GpioHelper.shutLED(riddle.getId());
		return Response.ok().header("X-riddle", generateRiddleUrl(uriInfo)).build();
	}

	private String generateAnswerUrl(UriInfo uriInfo, String id) {
		return uriInfo.getBaseUriBuilder().path("riddle").path(id).build().toString();
	}

	private String generateRiddleUrl(UriInfo uriInfo) {
		return uriInfo.getBaseUriBuilder().path("riddle").build().toString();
	}

	private void upgradeSettings() {
		Setting setting = fileManager.readSetting();
		setting.setRiddle(setting.getRiddle()+1);

		fileManager.writeToFile(setting);
	}

	private Riddle chooseRiddle() {
		Setting setting = fileManager.readSetting();
		return fileManager.readRiddle(setting.getRiddle());
	}
}
