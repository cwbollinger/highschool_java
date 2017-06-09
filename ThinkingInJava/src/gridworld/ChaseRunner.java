/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */
package gridworld;

import java.awt.Color;
import java.awt.Point;

import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Rock;

/**
 * This class runs a world that contains a bug and a rock, added at random
 * locations. Click on empty locations to add additional actors. Click on
 * populated locations to invoke methods on their occupants. <br />
 * To build your own worlds, define your own actors and a runner class. See the
 * BoxBugRunner (in the boxBug folder) for an example. <br />
 * This class is not tested on the AP CS A and AB exams.
 */
public class ChaseRunner {
	public static void main(String[] args) {
		ActorWorld world = new ActorWorld();
		Bug b1 = new Bug(Color.RED);
		Bug b2 = new Bug(Color.BLUE);
		world.add(b1);
		world.add(b2);
		world.add(new Rock());
		world.show();
		System.out.println(distance(b1, b2));
		moveBugs(b1, b2, 5);
	}

	public static double distance(Bug b1, Bug b2) {
		return Point
				.distance(b1.getLocation().getRow(), b1.getLocation().getCol(),
						b2.getLocation().getRow(), b2.getLocation().getCol());
	}

	public static void turnToward(Bug b1, Bug b2) {
		int y =b2.getLocation().getRow()- b1.getLocation().getRow();
		int x = b2.getLocation().getCol()- b1.getLocation().getCol();
		System.out.println(""+x+" "+y);
		double angle = Math.atan2(y,x);
		angle *= 180.0/Math.PI;
		angle = Math.round(angle)+90;
		System.out.println(angle);
		b1.setDirection((int)angle);
	}
	public static void moveToward(Bug b1, Bug b2) {
		turnToward(b1, b2);
		if(b1.canMove()) {
			b1.move();
		}
	}
	public static void moveBugs(Bug b1, Bug b2, int n) {
		for(; n > 0; n--) {
			moveToward(b1, b2);
			moveToward(b2, b1);
		}
	}
}
