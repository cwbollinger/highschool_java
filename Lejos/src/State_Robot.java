import lejos.nxt.*;

public class State_Robot {
	static NXTMotor motLeft = new NXTMotor(MotorPort.A);
	static NXTMotor motRight = new NXTMotor(MotorPort.B);
	static TouchSensor touch = new TouchSensor(SensorPort.S1);
	State state;
	boolean pressed;
	
	public State_Robot() {
		state = State.START;
		motLeft.setPower(50);
		motRight.setPower(50);
		pressed = false;
	}
	
	public enum State {
		START, FORWARD, BACKWARD;
	}
	
	public State Start() {
		Sound.beep();
		return State.FORWARD;
	}
	public State Forward() {
		motLeft.forward();
		motRight.forward();
		if(touch.isPressed()&&(pressed==false)) {
			pressed = true;
			return State.BACKWARD;
		}
		if(!touch.isPressed()) {
			pressed = false;
		}
		return State.FORWARD;
	}
	public State Backward() {
		motLeft.backward();
		motRight.backward();
		if(touch.isPressed()&&(pressed==false)) {
			pressed = true;
			return State.FORWARD;
		}
		if(!touch.isPressed()) {
			pressed = false;
		}
		return State.BACKWARD;
		
	}
	
	public void run() {
		while(Button.ESCAPE.isUp()) {
			if(state == State.START) {
				state = Start();
			}
			if(state == State.FORWARD) {
				state = Forward();
			}
			if(state == State.BACKWARD){
				state = Backward();
			}
		}
		
	}

	public static void main(String[] args) {
		State_Robot bot = new State_Robot();
		bot.run();
		
	}
}
