package de.adorsys.quiz.resource;

import de.adorsys.quiz.GpioHelper;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/question")
public class Quiz {

	@PostConstruct
	public void inti(){
		GpioHelper.start();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRiddle(@QueryParam("lvl") int lvl) {
		GpioHelper.shutLED(lvl);
		return Response.ok().build();
	}

}
