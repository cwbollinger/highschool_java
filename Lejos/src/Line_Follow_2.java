import lejos.nxt.*;
import lejos.nxt.comm.RConsole;


public class Line_Follow_2 {

	public static void main(String[] args) throws InterruptedException
	{
		int lightVal_1 = 0;
		int lightVal_2 = 0;
		
		NXTMotor motA = new NXTMotor(MotorPort.A);
		NXTMotor motB = new NXTMotor(MotorPort.B);
		
		motA.setPower(100);
		motB.setPower(100);
		
		
		LightSensor light_1 = new LightSensor(SensorPort.S2);
		LightSensor light_2 = new LightSensor(SensorPort.S3);
		
		//RConsole.openUSB(20000);
		//RConsole.println("Connected!");
		
		while(Button.ESCAPE.isUp()) {
			
			lightVal_1 = light_1.readValue();
			lightVal_2 = light_2.readValue();
			
			if(lightVal_1 < 50) {
				//RConsole.println("Right sensor black");
				motA.setPower(60);
				motB.setPower(30);
				motA.forward();
				motB.backward();
			}
			else if(lightVal_2 < 50){
				//RConsole.println("Left sensor black");
				motA.setPower(30);
				motB.setPower(60);
				motA.backward();
				motB.forward();
			} else {
				//RConsole.println("Both sensors white");
				motA.setPower(60);
				motB.setPower(60);
				motA.forward();
				motB.forward();
			}
		}
	}
}
