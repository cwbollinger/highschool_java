import ch.aplu.nxtsim.*;

public class First {
	First()
	{
		NxtRobot robot = new NxtRobot();
		Motor motA = new Motor(MotorPort.A);
		Motor motB = new Motor(MotorPort.B);
		
		robot.addPart(motA);
		robot.addPart(motB);
		
		motA.forward();
		motB.forward();
		Tools.delay(1000);
		motA.stop();
		Tools.delay(2000);
		robot.exit();
	}
	
	public static void main(String[] args)
	{
		new First();
	}
}
