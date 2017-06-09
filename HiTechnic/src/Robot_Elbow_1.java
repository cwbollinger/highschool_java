
import lejos.nxt.Button;
import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class Robot_Elbow_1 extends I2CSensor {

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

	public static final byte READ_BUFFER_ADDRESS = 0x42;
	public static final byte WRITE_BUFFER_ADDRESS = 0x4D;

	public static final double ANGLE_CONVERSION = 360.0 / 1024.0;

	public Robot_Elbow_1(I2CPort port) {
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
		byte[] readBuffer = new byte[2];
		int inputData = 0;
		String msg;
		int angle;

		boolean debug = false;

		if (debug) {
			RConsole.openUSB(10000);
		}

		while (!Button.ESCAPE.isPressed()) {
			this.getData(READ_BUFFER_ADDRESS, readBuffer, 2);
			inputData = Robot_Elbow_1.signedByteToInt(readBuffer[0]) * 4;
			inputData += Robot_Elbow_1.signedByteToInt(readBuffer[1]);

			/*
			 * LCD.clearDisplay();
			 * LCD.drawInt(Experiment2.signedByteToInt(readBuffer[0]), 0, 0);
			 * LCD.drawInt(Experiment2.signedByteToInt(readBuffer[1]), 1, 1);
			 * LCD.drawInt(inputData, 3, 3);
			 */
			// msg = String.format("In: %d", inputData);

			angle = (int) (inputData * ANGLE_CONVERSION);
			
			if (debug) {
				msg = "Raw: " + inputData;
				RConsole.println(msg);
				msg = "Angle: " + angle;
				RConsole.println(msg);
			}

			Motor.A.rotateTo(angle);
		}
	}

	public static void main(String[] args) {
		Robot_Elbow_1 instance = new Robot_Elbow_1(SensorPort.S4);
		instance.run();
	}
}
