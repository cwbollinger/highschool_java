

import lejos.nxt.Button;
import lejos.nxt.I2CSensor;
import lejos.nxt.I2CPort;
import lejos.nxt.SensorPort;

public class HiTechnic extends I2CSensor {

	private static final byte SENSOR_ADDRESS = 0x01;
	private static final byte CONTROL_REGISTER = 0x4E;
	private static final byte SAMPLE_FREQ = 0x3F;

	public static final byte READ_BUFFER_ADDRESS = 0x42;
	public static final byte WRITE_BUFFER_ADDRESS = 0x4D;

	public HiTechnic(I2CPort port) {
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

	public static void main(String[] args) {
		HiTechnic instance = new HiTechnic(SensorPort.S1);

		byte[] readBuffer = new byte[2];
		int inputData = 0;
		byte outputData = 0x00;

		while (!Button.ESCAPE.isPressed()) {
			instance.getData(READ_BUFFER_ADDRESS, readBuffer, 2);
			inputData = HiTechnic.signedByteToInt(readBuffer[0]) * 4;
			inputData += HiTechnic.signedByteToInt(readBuffer[1]);

			if (inputData < 512) {
				outputData = 0x00;
			} else {
				outputData = 0x01;
			}

			instance.sendData(WRITE_BUFFER_ADDRESS, outputData);
		}
	}
}
