import lejos.nxt.addon.*;
import lejos.nxt.*;

public class IRSeekerTest {
	NXTMotor motC;
	NXTMotor motB;
	NXTMotor motA;
	IRSeekerV2 seeker;

	public IRSeekerTest() {
		seeker = new IRSeekerV2(SensorPort.S3, IRSeekerV2.Mode.AC);
		motC = new NXTMotor(MotorPort.C);
		motB = new NXTMotor(MotorPort.B);
		motA = new NXTMotor(MotorPort.A);
		motC.setPower(70);
		motB.setPower(70);
		motA.setPower(100);
		motC.stop();
		motB.stop();
		motA.forward();
	}

	public void faceBall() {
		int dir = seeker.getDirection();
		while (true) {
			
			if (dir > 5) {
				motC.forward();
				motB.backward();
			} else if(dir < 5){
				motB.forward();
				motC.backward();
			} else {
				if(seeker.getSensorValue(3) > 210) {
					motB.stop();
					motC.stop();
				} else {
					motB.forward();
					motC.forward();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dir = seeker.getDirection();
		}
		
	}

	public static void main(String[] args) {

		new IRSeekerTest().faceBall();

	}
}