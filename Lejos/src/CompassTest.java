//package org.lejos.sample.compasstest;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;

/**
 * Simple test of compass sensors.
 * 
 * Works with Mindsensors and HiTechnic compass sensors.
 * 
 * @author Lawrie Griffiths
 * 
 */
public class CompassTest {

	public static void main(String[] args) throws Exception {
		CompassHTSensor compass = new CompassHTSensor(SensorPort.S4);

		boolean calibrate = true;

		if (calibrate) {
			LCD.drawString("Calibrate:", 0, 0);
			while (!Button.ENTER.isDown()) {
			}
			while (Button.ENTER.isDown()) {
			}
			compass.startCalibration();
		}
		else {
			while (!Button.ESCAPE.isDown()) {
				LCD.clear();
				LCD.drawString("Bearing:", 0, 0);
				LCD.drawInt((int) compass.getDegrees(), 0, 1);
				Thread.sleep(500);
			}
		}

		if (calibrate) {
			while (!Button.ENTER.isDown()) {
			}
			while (Button.ENTER.isDown()) {
			}
			compass.stopCalibration();
		}
	}
}
