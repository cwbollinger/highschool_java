import lejos.nxt.*;
import lejos.nxt.comm.RConsole;


public class Line_Follow_1 {

	public static void main(String[] args) throws InterruptedException
	{
		int lightVal = 0;
		
		NXTMotor motA = new NXTMotor(MotorPort.A);
		NXTMotor motB = new NXTMotor(MotorPort.B);
		
		motA.setPower(100);
		motB.setPower(100);
		
		
		
		LightSensor touch = new LightSensor(SensorPort.S1);
		
		//RConsole.openUSB(20000);
		//RConsole.println("Connected!");
		
		while(Button.ESCAPE.isUp()) {
			lightVal = touch.readValue();
			if(lightVal < 50) {
				//RConsole.println("black");
				motA.setPower(60);
				motB.setPower(30);
				motA.forward();
				motB.backward();
			}
			else {
				//RConsole.println("white");
				motA.setPower(30);
				motB.setPower(60);
				motA.backward();
				motB.forward();
			}
		}
	}
}
