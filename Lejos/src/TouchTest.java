import lejos.nxt.*;
import lejos.nxt.comm.RConsole;

public class TouchTest {
	
	public static void main(String[] args) throws InterruptedException
	{
		boolean touchVal = false;
		
		NXTMotor motA = new NXTMotor(MotorPort.A);
		NXTMotor motB = new NXTMotor(MotorPort.B);
		
		motA.setPower(50);
		motB.setPower(50);
		
		TouchSensor touch = new TouchSensor(SensorPort.S1);
		
		//RConsole.openUSB(20000);
		//RConsole.println("Connected!");
		
		while(true) {
			touchVal = touch.isPressed();
			if(touchVal) {
				//RConsole.println("touch");
				motA.backward();
				motB.backward();
				Thread.sleep(1000);
				motB.forward();
				Thread.sleep(1000);
			}
			else {
				//RConsole.println("no touch");
				motA.forward();
				motB.forward();
			}
		}
	}
	
}
