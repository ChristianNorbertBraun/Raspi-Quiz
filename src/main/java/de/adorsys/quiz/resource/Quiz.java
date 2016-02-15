package de.adorsys.quiz.resource;

import com.google.gson.Gson;
import de.adorsys.quiz.GpioHelper;
import de.adorsys.quiz.entity.Answer;
import de.adorsys.quiz.entity.Riddle;
import de.adorsys.quiz.entity.Setting;

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Path("/riddle")
public class Quiz {

	private Gson gson;

	@PostConstruct
	public void inti() {
		GpioHelper.start();
		gson = new Gson();
	}

	@GET
	@Path("/lvl/{lvl}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRiddle(@PathParam("lvl") int lvl, @Context UriInfo uriInfo) {
		Riddle riddle = chooseRiddle(lvl);
		return Response.ok(riddle).header("X-answer", generateAnswerUrl(uriInfo, riddle.getLevel(), riddle.getId())).build();
	}

	@PUT
	@Path("/{riddleId}/lvl/{lvl}")
	public Response answerRiddle(@PathParam("riddleId") int riddleId, @PathParam("lvl") int lvl, @Context UriInfo uriInfo, Answer answer) {
		String pathToFile = getPathToRiddle(riddleId, lvl);
		Riddle riddle = gson.fromJson(readFromFile(pathToFile), Riddle.class);
		if (!answer.getAnswer().toLowerCase().equals(riddle.getAnswer().toLowerCase()))
			return Response.status(Response.Status.BAD_REQUEST).build();
		upgradeSettings(lvl);
		GpioHelper.shutLED(riddle.getLevel());
		return Response.ok()
				.header("X-lvl-1", generateRiddleUrl(uriInfo, 1))
				.header("X-lvl-2", generateRiddleUrl(uriInfo, 2))
				.header("X-lvl-3", generateRiddleUrl(uriInfo, 3))
				.build();
	}

	private String generateAnswerUrl(UriInfo uriInfo, int lvl, String id) {
		return uriInfo.getBaseUriBuilder().path("riddle").path(id).path("lvl").path(String.valueOf(lvl)).build().toString();
	}

	private String generateRiddleUrl(UriInfo uriInfo, int lvl) {
		return uriInfo.getBaseUriBuilder().path("riddle").path("lvl").path(String.valueOf(lvl)).build().toString();
	}

	private void upgradeSettings(int lvl) {
		Setting setting = gson.fromJson(readFromFile("settings.json"), Setting.class);
		setting.setPoints(setting.getPoints() + lvl);
		switch (lvl) {
			case 1:
				setting.setLvl_1(setting.getLvl_1() + 1);
				break;
			case 2:
				setting.setLvl_2(setting.getLvl_2() + 1);
				break;
			case 3:
				setting.setLvl_3(setting.getLvl_3() + 1);
				break;
		}

		writeToFile(setting);
	}

	private Riddle chooseRiddle(int lvl) {
		Setting setting = gson.fromJson(readFromFile("settings.json"), Setting.class);
		int riddleId;
		switch (lvl) {
			case 1:
				riddleId = setting.getLvl_1();
				break;
			case 2:
				riddleId = setting.getLvl_2();
				break;
			case 3:
				riddleId = setting.getLvl_3();
				break;
			default:
				riddleId = -1;
				break;
		}
		return gson.fromJson(readFromFile(getPathToRiddle(riddleId, lvl)), Riddle.class);
	}

	private String getPathToRiddle(int id, int lvl) {
		return String.format("lvl_%d/%d.json", lvl, id);
	}

	private String readFromFile(String pathToFile) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			FileReader fileReader = new FileReader(classLoader.getResource(pathToFile).getFile());
			BufferedReader reader = new BufferedReader(fileReader);

			String temp;
			StringBuilder text = new StringBuilder();
			while ((temp = reader.readLine()) != null) {
				text.append(temp);
			}
			return text.toString();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "";
		}
	}

	private void writeToFile(Setting setting) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			FileWriter fileWriter = new FileWriter(classLoader.getResource("settings.json").getFile(),false);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			try {

				writer.write(gson.toJson(setting));

			} catch (Exception ex) {
				System.out.print(ex.getMessage());
			} finally {
				writer.flush();
			}
		} catch (IOException ex) {
			//die
		}
	}
}
