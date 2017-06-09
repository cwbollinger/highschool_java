import lejos.nxt.*;


public class MotorTest {
	
	
	public MotorTest() {
	}
	
	public static void main(String[] args) throws InterruptedException{
		int i = 0;

		NXTMotor motA = new NXTMotor(MotorPort.A);
		NXTMotor motB = new NXTMotor(MotorPort.B);
		
		motA.setPower(100);
		motB.setPower(100);
		motA.forward();
		motB.backward();
		
		while(true);
		
//		while(true) {
//			for(; i < 100; i++) {
//				motA.setPower(i);
//				motB.setPower(i);
//				Thread.sleep(100);
//			}
//			for(; i > -100; i--) {
//				motA.setPower(i);
//				motB.setPower(i);
//				Thread.sleep(100);
//			}
//		}
	}
}
