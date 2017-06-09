// http://lejos.sourceforge.net/forum/viewtopic.php?t=1774&highlight=pcf8574

import lejos.nxt.I2CPort;
import lejos.nxt.SensorPort;

/**
 * Access to a PCF8574 chip, which is a parallel port extension via I2C.
 */
public class PCF8574_lejos09 {

	/** Port where the chip is attached to */
	private final I2CPort port;

	/** Device address of the chip */
	private final int address;

	/** Buffer for i/o */
	private byte buffer[] = { 0x00 };

	/**
	 * Constructor
	 * 
	 * @param i2cPort
	 *            Port wheere the chip is attached to (e.g. SensorPort.S1)
	 * @param deviceAddress
	 *            Device address of the chip (e.g. 0x40)
	 */
	public PCF8574_lejos09(I2CPort i2cPort, int deviceAddress) {
		port = i2cPort;
		address = deviceAddress;
		port.i2cEnable(I2CPort.STANDARD_MODE);
	}

	/**
	 * Write to the i/o pins
	 * 
	 * @param value
	 *            Data to be written
	 */
	public void write(byte value) {
		buffer[0] = value;
		port.i2cTransaction(address, buffer, 0, 1, null, 0, 0);
	}

	/**
	 * Read from the i/o pins
	 * 
	 * @return Data that have been read
	 */
	public byte read() {
		port.i2cTransaction(address, null, 0, 0, buffer, 0, 1);
		return buffer[0];
	}

	public static void main(String[] args) {

		// PCF8574 Address for magic wand
		final int I2CAddr8574 = 0x70;

		PCF8574_lejos09 wand = new PCF8574_lejos09(SensorPort.S1, I2CAddr8574);
		wand.write((byte) 0xFF);
		/*
		for (byte i = 0; i < 255; i++) {
			wand.write(i);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
	}

}
