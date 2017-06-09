import lejos.nxt.*;
import lejos.nxt.comm.RConsole;

public class LightTest {
	
	public static void main(String[] args) throws InterruptedException
	{
		int lightVal1 = 0;
		int lightVal2 = 0;
		
		LightSensor light1 = new LightSensor(SensorPort.S2);
		LightSensor light2 = new LightSensor(SensorPort.S3);
		
		//RConsole.openUSB(20000);
		//RConsole.println("Connected!");
		
		while(Button.ESCAPE.isUp()) {
			lightVal1 = light1.getLightValue();
			lightVal2 = light2.getLightValue();
			LCD.drawString("Sensor 1:" + lightVal1, 0, 0);
			LCD.drawString("Sensor 2:" + lightVal2, 0, 2);
		}
	}
	
}
