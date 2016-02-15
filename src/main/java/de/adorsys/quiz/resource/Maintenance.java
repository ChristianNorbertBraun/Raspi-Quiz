package de.adorsys.quiz.resource;

import de.adorsys.quiz.entity.Setting;
import de.adorsys.quiz.helper.GpioHelper;
import de.adorsys.quiz.helper.SettingsHelper;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/maintenance")
public class Maintenance {

	private SettingsHelper helper;

	@PostConstruct
	public void init() {
		helper = new SettingsHelper();
	}

	@GET
	public Response reset() {
		GpioHelper.restart();
		Setting setting = new Setting(0,0,0,0);
		if (helper.writeToFile(setting))
			return Response.ok().build();
		return Response.status(Response.Status.BAD_REQUEST).build();

	}

	@GET
	@Path("/points")
	public Response points() {
		Setting setting = helper.readSetting();

		return Response.ok(setting.getPoints()).build();
	}
}
