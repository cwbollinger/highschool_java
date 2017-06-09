import lejos.nxt.*;
import lejos.nxt.comm.RConsole;


public class JP_Line_Follow_1 {

	public static void main(String[] args) throws InterruptedException {
		
		NXTMotor motA = new NXTMotor(MotorPort.A);
		NXTMotor motB = new NXTMotor(MotorPort.B);
		LightSensor light = new LightSensor(SensorPort.S3);
		
		int lightVal;
		String msg;
		//Kc = 10 Pc = .3 dT = .00159
		final double Kp = 6;
		final double Ki = 0.0636;
		final double Kd = 141.5;
		final int Tp = 75;
		final int offset = 40; //value of black reading: sets zero error.
		
		int integral = 0;
		int lastError = 0;
		int derivative = 0;
		int error = 0;
		
		int Turn = 0;
		int powerA = 0;
		int powerB = 0;
		
		
		//RConsole.openUSB(20000);
		//RConsole.println("Connected!");
		Thread.sleep(5000);
		
		while(Button.ESCAPE.isUp()) {
			lightVal = light.getLightValue();
			
			if(error == 0) {
				integral = 0;
			}
			
		    error = lightVal-offset;
		    integral = integral + error;
			derivative = error-lastError;
			Turn = (int)(Kp*error+Ki*integral+Kd*derivative);
			powerA = Tp+Turn;
			powerB = Tp-Turn;
			
			msg = "" + Kp*error + ", " + Ki*integral;
			//RConsole.println(msg);

			
			if(powerA>0){
			motA.setPower(powerA);
			motA.forward();
			} else {
				powerA=powerA*(-1);
				motA.setPower(powerA);
				motA.backward();
			}
			if(powerB>0){
				motB.setPower(powerB);
				motB.forward();
				} else {
					powerB=powerB*(-1);
					motB.setPower(powerB);
					motB.backward();
				}
			
			lastError = error;
		}
		
	}
	
}

