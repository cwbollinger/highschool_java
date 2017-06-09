// Author: Chris

import java.util.Scanner;

public class Exercise6_5 {

	public double sumSquares(double num1, double num2) {
		return Math.pow(num1, 2)+Math.pow(num2, 2);
	}
	
	public void distance(double x1, double y1, double x2, double y2) {
		double dist = Math.sqrt(sumSquares((x2-x1), (y2-y1)));
		System.out.println("Distance between points: " + dist);
		return;
	}
	
	public Point getPoint() {
		Scanner input = new Scanner(System.in);
		System.out.println("X and Y: ");
		double x = input.nextDouble();
		double y = input.nextDouble();
		return new Point(x, y);
	}
	
	public static void main(String[] args) {
		Point point1, point2;
		
		Exercise6_5 problem = new Exercise6_5();
		System.out.print("Enter First point ");
		point1 = problem.getPoint();
		System.out.print("Enter Second point ");
		point2 = problem.getPoint();
		
		System.out.println("You entered: " + point1.toString() + " " + point2.toString());
		
		problem.distance(point1.x(), point1.y(), point2.x(), point2.y());
	}
}