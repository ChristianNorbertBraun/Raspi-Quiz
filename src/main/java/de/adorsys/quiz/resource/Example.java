package de.adorsys.quiz.resource;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/example")
public class Example {

	@Context
	UriInfo uriInfo;

	static final GpioController gpio = GpioFactory.getInstance();

	// creating the pin with parameter PinState.HIGH
	// will instantly power up the pin
	static GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PinLED", PinState.HIGH);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response blink() {
		try {
			// get a handle to the GPIO controller

			System.out.println("light is: ON");

			// wait 2 seconds
			Thread.sleep(2000);

			// turn off GPIO 1
			pin.low();
			System.out.println("light is: OFF");
			// wait 1 second
			Thread.sleep(1000);
			// turn on GPIO 1 for 1 second and then off
			System.out.println("light is: ON for 1 second");
			pin.pulse(1000, true);

			// release the GPIO controller resources
			gpio.shutdown();
		} catch (InterruptedException ex) {
			System.out.println("nö");
		}
		return Response.ok().build();
	}

}