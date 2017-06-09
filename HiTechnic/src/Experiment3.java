

import lejos.nxt.Button;
import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

public class Experiment3 extends I2CSensor {

	private static final byte SENSOR_ADDRESS = 0x01;
	private static final byte CONTROL_REGISTER = 0x4E;
	private static final byte SAMPLE_FREQ = 0x3F;

	private static final byte BIT_NULL = 0x00;
	private static final byte BIT_0 = 0x01;
	private static final byte BIT_1 = 0x02;
	private static final byte BIT_2 = 0x04;
	private static final byte BIT_3 = 0x08;
	private static final byte BIT_4 = 0x10;
	private static final byte BIT_5 = 0x20;

	private static final byte WRITE_BUFFER_ADDRESS = 0x4D;

	public Experiment3(I2CPort port) {
		super(port);
		port.setType(TYPE_LOWSPEED_9V);
		setAddress(SENSOR_ADDRESS);
		setUpSensor();
	}

	public static int signedByteToInt(byte abyte) {
		return (int) abyte & 0xff;
	}

	private int setUpSensor() {
		return sendData(CONTROL_REGISTER, SAMPLE_FREQ);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Experiment3 instance = new Experiment3(SensorPort.S4);

		SoundSensor soundSensor = new SoundSensor(SensorPort.S2, true);

		int inputData = 0;
		byte outputData = BIT_NULL;

		while (!Button.ESCAPE.isPressed()) {

			inputData = soundSensor.readValue() * 10;

			LCD.clearDisplay();
			LCD.drawInt(inputData, 3, 3);

			outputData = BIT_NULL;

			if (inputData > 15) {
				outputData |= BIT_0;
			}
			if (inputData > 31) {
				outputData |= BIT_1;
			}
			if (inputData > 63) {
				outputData |= BIT_2;
			}
			if (inputData > 127) {
				outputData |= BIT_3;
			}
			if (inputData > 255) {
				outputData |= BIT_4;
			}
			if (inputData > 511) {
				outputData |= BIT_5;
			}

			instance.sendData(WRITE_BUFFER_ADDRESS, outputData);
		}
		instance.sendData(WRITE_BUFFER_ADDRESS, BIT_NULL);
	}
}
