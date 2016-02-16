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

	public static void start() {
		final GpioController gpio = GpioFactory.getInstance();
		if (pin17 == null)
			pin17 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Pin17", PinState.HIGH);
		if (pin18 == null)
			pin18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Pin18", PinState.HIGH);
		if (pin27 == null)
			pin27 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Pin27", PinState.HIGH);
		gpio.shutdown();
	}

	public static void restart() {
		final GpioController gpio = GpioFactory.getInstance();
		if (pin17 == null || pin18 == null || pin27 == null)
			start();
		pin17.high();
		pin18.high();
		pin27.high();
		gpio.shutdown();
	}

	public static void shutLED(int number) {
		pin17.low();
		if (number > 1) {
			pin18.low();
		}
		if (number > 2) {
			pin27.low();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					//no worries
				} finally {
					restart();
				}
			}
		}).start();
	}
}
