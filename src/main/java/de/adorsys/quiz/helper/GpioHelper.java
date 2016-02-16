package de.adorsys.quiz.helper;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GpioHelper {

	public static GpioPinDigitalOutput pin17;
	public static GpioPinDigitalOutput pin18;
	public static GpioPinDigitalOutput pin27;
	public static GpioPinDigitalOutput pin22;
	public static GpioPinDigitalOutput pin23;
	public static GpioPinDigitalOutput pin24;

	public static void start() {
		final GpioController gpio = GpioFactory.getInstance();
		if (pin17 == null)
			pin17 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Pin17", PinState.HIGH);
		if (pin18 == null)
			pin18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Pin18", PinState.HIGH);
		if (pin27 == null)
			pin27 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Pin27", PinState.HIGH);
		if (pin22 == null)
			pin22 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Pin22", PinState.HIGH);
		if (pin23 == null)
			pin23 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Pin23", PinState.HIGH);
		if (pin24 == null)
			pin24 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin24", PinState.HIGH);
		gpio.shutdown();
	}

	public static void restart() {
		final GpioController gpio = GpioFactory.getInstance();
		if (pin17 == null || pin18 == null || pin27 == null || pin22 == null || pin23 == null || pin24 == null)
			start();
		pin17.high();
		pin18.high();
		pin27.high();
		pin22.high();
		pin23.high();
		pin24.high();
		gpio.shutdown();
	}

	public static void shutLED(String id) {
		switch (id) {
			case "0":
				pin17.low();
				break;
			case "1":
				pin18.low();
				break;
			case "2":
				pin27.low();
				break;
			case "3":
				pin22.low();
				break;
			case "4":
				pin23.low();
				break;
			case "5":
				pin24.low();
				break;
		}
	}
}
