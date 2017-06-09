import lejos.nxt.addon.*;
import lejos.nxt.*;

public class UltrasonicTest {
	
	public static void main(String args[]) {
		NXTMotor B = new NXTMotor(MotorPort.B);
		B.setPower(70);
		NXTMotor C = new NXTMotor(MotorPort.C);
		C.setPower(70);
		UltrasonicSensor US = new UltrasonicSensor(SensorPort.S1);
		US.continuous();
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			System.err.println(e);
		}
		
		
		while(true) {
			if(US.getDistance() > 7) {
				B.backward();
				C.backward();
				try {
					Thread.sleep(100);
				} catch(Exception e) {
					System.err.println(e);
				}
				B.stop();
				try {
					Thread.sleep(100);
				} catch(Exception e) {
					System.err.println(e);
				}
			} else {
				B.forward();
				C.forward();
			}
		}
		
	}
}
