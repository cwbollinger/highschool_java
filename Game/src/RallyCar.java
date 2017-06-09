import com.ibm.rally.Car;
import com.ibm.rally.ICar;
import com.ibm.rally.IObject;
/**
 * This is the class that you must implement to enable your car within
 * the CodeRally track. Adding code to these methods will give your car
 * it's personality and allow it to compete.
 */
public class RallyCar extends Car {
	protected static final double kP = 0.6;
	protected int state;
	protected int h;
	protected int n;
	protected int x;
	protected double enemy_dist;
	protected int enemy_heading;
	protected int station_num;
	protected int heading_diff;
	protected int throttle_speed;
	protected int print_state;
	protected int fuel_factor;

	/**
	 * @see com.ibm.rally.Car#getColor()
	 */
	public byte getColor() {
		return CAR_YELLOW;
	}

	/**
	 * @see com.ibm.rally.Car#initialize()
	 */
	public void initialize() {
		state = 1;
		n = 0;
		print_state = 0;
		fuel_factor = 1;
		
	}
	
	public int FindGas() {
		double x = getDistanceTo(getFuelDepots()[0]);
		double y = getDistanceTo(getFuelDepots()[1]); 
		double z = getDistanceTo(getFuelDepots()[2]);

		if(x < y) {
			if(x < z) {
				return 0;
			} else {
				return 2;
			}
		} else {
			return 1;
		}
	}
	
	public int FindTire() {
		double x = getDistanceTo(getSpareTireDepot()[0]);
		double y = getDistanceTo(getSpareTireDepot()[1]); 
		double z = getDistanceTo(getSpareTireDepot()[2]);

		if(x < y) {
			if(x < z) {
				return 0;
			} else {
				return 2;
			}
		} else {
			return 1;
		}
	}

	/**
	 * @see com.ibm.rally.Car#move(int, boolean, ICar, ICar)
	 * Put the car in reverse for a few moves if you collide with another car.
	 * Go toward the first gas depot.
	 */
	
	public void move(int lastMoveTime, boolean hitWall, ICar collidedWithCar, ICar hitBySpareTire) {

		if(state == 1) {
			if(print_state != 1) {
				System.out.println("checkpoint");
				print_state = 1;
			}
			setThrottle(100);
			
			//determine next checkpoint
			if(getPreviousCheckpoint() == (getCheckpoints().length-1)) {
				h = getHeadingTo(getCheckpoints()[0]);
			} else {
				h = getHeadingTo(getCheckpoints()[getPreviousCheckpoint()+1]);
			}
			
			// P algorithm with north-spinning correction
			heading_diff = getHeading() - h;
			if(heading_diff > 180) {
				heading_diff = -(360 - heading_diff);
			}
			if(heading_diff < -180) {
				heading_diff = 360 + heading_diff;
			}
			x = (int) (heading_diff*kP);
			if(x > 10) {
				x = 10;
			} else if(x < -10) {
				x = -10;
			}
			setSteeringSetting(-x);
			
			//switch to refueling behavior when gas is low and there's more than 10 seconds left
			if((getFuel() < 15*fuel_factor) && (getClockTicks() < 500)) {
				state = 2;
			}
			//switch to tire reloading behavior
			if((getNumberOfSpareTires() == 0) && (getClockTicks() < 400)) {
				state = 3;
			}
			//enter protect mode if another car gets too close
			if(!isInProtectMode()) {
				fuel_factor = 1;
				n = getOpponents().length;
				for(; n > 0; n--) {
					enemy_dist = getDistanceTo(getOpponents()[n-1]);
					if(enemy_dist < 100) {
						state = 4;
						break;
					}
				}
			}
			//throw a spare tire at any opponents in front of the car
			if(isReadyToThrowSpareTire()) {
				n = getOpponents().length;
				for(; n > 0; n--) {
					enemy_heading = getHeadingTo(getOpponents()[n-1]);
					enemy_dist = getDistanceTo(getOpponents()[n-1]);
					if((getHeading() == enemy_heading)&&(enemy_dist < 300)) {
						state = 5;
						break;
					}
				}
			}
		}
		//refueling state
		if(state == 2) {
			if(print_state != 2) {
				System.out.println("refueling");
				print_state = 2;
			}
			station_num = FindGas();
			
			//ramp-down throttle as distance to closest fuel depot decreases
			throttle_speed = (int) (getDistanceTo(getFuelDepots()[station_num])/2);
			if(throttle_speed > 100) {
				throttle_speed = 100;
			}
			if(throttle_speed < 15) {
				throttle_speed = 0;
			}
			setThrottle(throttle_speed);
		
			//determine heading to closest fuel depot
			h = getHeadingTo(getFuelDepots()[station_num]);
			
			// P algorithm with north-spinning correction
			heading_diff = getHeading() - h;
			if(heading_diff > 180) {
				heading_diff = 360 - heading_diff;
			}
			if(heading_diff < -180) {
				heading_diff = -360 - heading_diff;
			}
			
			x = (int) (heading_diff*kP);
			if(x > 10) {
				x = 10;
			} else if(x < -10) {
				x = -10;
			}
			setSteeringSetting(-x);
	
			//when tank is refilled, return to racing
			if(getFuel() > 70) {
				state = 1;
			}
		}
		//reload spare tires
		if(state == 3) {
			if(print_state != 3) {
				System.out.println("tire reload");
				print_state = 3;
			}
			station_num = FindTire();
			
			//ramp-down throttle as distance to closest tire depot decreases
			throttle_speed = (int) getDistanceTo(getSpareTireDepot()[station_num]);
			if(throttle_speed > 100) {
				throttle_speed = 100;
			}
			if(throttle_speed < 30) {
				throttle_speed = 0;
			}
			setThrottle(throttle_speed);
			
			//determine heading to closest tire depot
			h = getHeadingTo(getFuelDepots()[station_num]);
			
			// P algorithm with north-spinning correction
			heading_diff = getHeading() - h;
			if(heading_diff > 180) {
				heading_diff = 360 - heading_diff;
			}
			if(heading_diff < -180) {
				heading_diff = -360 - heading_diff;
			}
			
			x = (int) (heading_diff*kP);
			if(x > 10) {
				x = 10;
			} else if(x < -10) {
				x = -10;
			}
			setSteeringSetting(-x);
	
			if(getNumberOfSpareTires() > 3) {
				state = 1;
			}
		}

		//enter protect mode
		if(state == 4) {
			if(print_state != 4) {
				System.out.println("protecting");
				print_state = 4;
			}
			if(enterProtectMode()) {
				fuel_factor = 2;
				state = 1;
			}
		}
		//fire spare tire
		if(state == 5) {
			if(print_state != 5) {
				System.out.println("Fire!");
				print_state = 5;
			}
			if(throwSpareTire()) {
				state = 1;
			}
		}
		
	}
}