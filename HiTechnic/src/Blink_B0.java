

import lejos.nxt.Button;
import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
//import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class Blink_B0 extends I2CSensor {

	private static final byte SENSOR_ADDRESS = 0x01;
	private static final byte CONTROL_REGISTER = 0x4E;
	private static final byte SAMPLE_FREQ = 0x3F;

	private static final byte BIT_NULL = 0x00;
	private static final byte BIT_0 = 0x01;
	/*
	private static final byte BIT_1 = 0x02;
	private static final byte BIT_2 = 0x04;
	private static final byte BIT_3 = 0x08;
	private static final byte BIT_4 = 0x10;
	private static final byte BIT_5 = 0x20;
	*/

	public static final byte READ_BUFFER_ADDRESS = 0x42;
	public static final byte WRITE_BUFFER_ADDRESS = 0x4D;

	public Blink_B0(I2CPort port) {
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

	public void run() {
		boolean toggle = true;
		byte outputData = BIT_NULL;

		while (!Button.ESCAPE.isPressed()) {
			/*
			LCD.clearDisplay();
			LCD.drawInt(Blink_B0.signedByteToInt(readBuffer[0]), 0, 0);
			LCD.drawInt(Blink_B0.signedByteToInt(readBuffer[1]), 1, 1);
			LCD.drawInt(inputData, 3, 3);
			*/

			if (toggle) {
				outputData |= BIT_0;
			}
			else {
				outputData= BIT_NULL;
			}
			this.sendData(WRITE_BUFFER_ADDRESS, outputData);
			
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				// Ignore
			}
			toggle = !toggle;
		}
		this.sendData(WRITE_BUFFER_ADDRESS, BIT_NULL);
	}

	public static void main(String[] args) {
		Blink_B0 instance = new Blink_B0(SensorPort.S4);
		instance.run();
	}
}
