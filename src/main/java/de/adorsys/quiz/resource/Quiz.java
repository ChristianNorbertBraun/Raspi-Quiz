package de.adorsys.quiz.resource;

import com.google.gson.Gson;
import de.adorsys.quiz.GpioHelper;
import de.adorsys.quiz.entity.Answer;
import de.adorsys.quiz.entity.Riddle;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Path("/riddle")
public class Quiz {

	@PostConstruct
	public void inti(){
		GpioHelper.start();
	}

	@GET
	@Path("/lvl/{lvl}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRiddle( @PathParam("lvl") int lvl) {
		return Response.ok(readFromFile(lvl, 1)).build();
	}

	@PUT
	@Path("/{riddleId}/lvl/{lvl}")
	public Response answerRiddle( @PathParam("riddleId") int riddleId, @PathParam("lvl") int lvl, Answer answer) {
		Riddle riddle = readFromFile(lvl, riddleId);
		if (!answer.getAnswer().toLowerCase().equals(riddle.getAnswer().toLowerCase()))
			return Response.status(Response.Status.BAD_REQUEST).build();

		GpioHelper.shutLED(riddle.getLevel());
		return Response.ok().build();
	}





	private Riddle readFromFile(int lvl, int riddleId) {
		String pathToFile = String.format("lvl_%d/%d.json", lvl, riddleId);
		Gson gson = new Gson();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(pathToFile).getFile());
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

			String temp;
			StringBuilder text = new StringBuilder();
			while ((temp = reader.readLine()) != null) {
				text.append(temp);
			}
			return gson.fromJson(text.toString(), Riddle.class);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
}
