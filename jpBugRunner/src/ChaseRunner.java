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

import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Rock;
import java.awt.Color;
//import info.gridworld.grid.UnboundedGrid;


/**
 * This class runs a world that contains a bug and a rock, added at random
 * locations. Click on empty locations to add additional actors. Click on
 * populated locations to invoke methods on their occupants. <br />
 * To build your own worlds, define your own actors and a runner class. See the
 * BoxBugRunner (in the boxBug folder) for an example. <br />
 * This class is not tested on the AP CS A and AB exams.
 */
public class ChaseRunner
{
  public static void moveBug(Bug bug, int n)
  {
	
	while(n > 0) {
	  if(bug.canMove()) {
        bug.move();
	  }
	  else{
		bug.turn();
	  }
	  n--;
	}
  }
  public static void randomBug(Bug bug, int n) 
  {
	int rand;
	
	for(; n > 0; n--) {
      rand = RandomCW.diceRoll(4);
      bug.setDirection(90*(rand-1));
      if(bug.canMove()) {
        bug.move();
      }
	}
  }
  public static Bug colorBug(Bug bug)
  {
    int x = bug.getLocation().getCol();
    while(x > 255) {
      x -= 255;
    }
    int y = bug.getLocation().getRow();
    while(y > 255) {
    	y -= 255;
    }
    Color temp = new Color(y, x, 0);
    bug.setColor(temp);
    return(bug);
  }
  public static void makeBug(ActorWorld world, int n) {
    Bug temp;
    for(; n > 0; n--) {
      world.add(temp = new Bug());
      colorBug(temp);
    }
  }
  public static double sumSquares(double num1, double num2) {
    return Math.pow(num1, 2)+Math.pow(num2, 2);
  }
  public static double distance(Bug bug1, Bug bug2) {
	double x1 = bug1.getLocation().getCol();
	double x2 = bug2.getLocation().getCol();
	double y1 = bug1.getLocation().getRow();
	double y2 = bug2.getLocation().getRow();
	
    double dist = Math.sqrt(sumSquares((x2-x1), (y2-y1)));
	return dist;
  }
  public static void turnToward (Bug bug1, Bug bug2) {
	  double xdiff = bug2.getLocation().getCol() - bug1.getLocation().getCol();
	  double ydiff = bug2.getLocation().getRow() - bug1.getLocation().getRow();
	  int angle = (int) (Math.atan(ydiff/xdiff)/Math.PI*180.0);
	  if(xdiff < 0) {
		  angle += 180;
	  }
	  bug1.setDirection(angle + 90);
  }
  public static void moveToward (Bug bug1, Bug bug2) {
	  turnToward(bug1, bug2);
	  if(bug1.canMove()) {
		  bug1.move();
	  }
  }
  public static void main(String[] args)
  {
    ActorWorld world = new ActorWorld();
    Bug redBug = new Bug(Color.red);
    Bug blueBug = new Bug(Color.cyan);
    world.add(redBug);
    world.add(blueBug);
    world.show();
    while(true) {
      moveToward(redBug, blueBug);
    }


//    colorBug(redBug);
//    colorBug(blueBug);
  }   
}
