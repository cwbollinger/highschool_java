import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import javax.swing.JFrame;

public class TestFractal extends JFrame {

	private List<Integer> turns;
	private double startingAngle, side;
	private int xStart, yStart;
	private Color color;
	
	public static Color[] colors = { 
		Color.BLUE,
		Color.GREEN,
		Color.RED,
		Color.ORANGE
	};

	public TestFractal(int iter) {
		super("Test Fractal");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		xStart = 230;
		yStart = 350;
		color = Color.BLACK;
		turns = getSequence(iter);
		startingAngle = -iter * (Math.PI / 4);
		side = 400 / Math.pow(2, iter / 2.);

	}

	public TestFractal(int iter, Color color) {
		super("Dragon Curve");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		xStart = 230;
		yStart = 350;
		setColor(color);
		turns = getSequence(iter);
		startingAngle = -iter * (Math.PI / 4);
		side = 400 / Math.pow(2, iter / 2.);

	}

	public TestFractal(int iter, int x, int y) {
		super("Dragon Curve");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		xStart = x;
		yStart = y;
		setColor(color);
		turns = getSequence(iter);
		startingAngle = -iter * (Math.PI / 4);
		side = 400 / Math.pow(2, iter / 2.);

	}

	public TestFractal(int iter, int x, int y, Color color) {
		super("Dragon Curve");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		xStart = x;
		yStart = y;
		setColor(color);
		turns = getSequence(iter);
		startingAngle = -iter * (Math.PI / 4);
		side = 400 / Math.pow(2, iter / 2.);

	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getxStart() {
		return xStart;
	}

	public void setxStart(int xStart) {
		this.xStart = xStart;
	}

	public int getyStart() {
		return yStart;
	}

	public void setyStart(int yStart) {
		this.yStart = yStart;
	}

	public List<Integer> getSequence(int iterations) {
		List<Integer> turnSequence = new ArrayList<Integer>();
		for (int i = 0; i < iterations; i++) {
			List<Integer> copy = new ArrayList<Integer>(turnSequence);
			Collections.reverse(copy);
			turnSequence.add(1);
			for (Integer turn : copy) {
				turnSequence.add(-turn);
			}
		}
		return turnSequence;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(color);
		double angle = startingAngle;
		int x1 = xStart, y1 = yStart;
		int x2 = x1 + (int) (Math.cos(angle) * side);
		int y2 = y1 + (int) (Math.sin(angle) * side);
		g.drawLine(x1, y1, x2, y2);
		x1 = x2;
		y1 = y2;
		for (Integer turn : turns) {
			angle += turn * (Math.PI / 2);
			x2 = x1 + (int) (Math.cos(angle) * side);
			y2 = y1 + (int) (Math.sin(angle) * side);
			g.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
	}

	public static void main(String[] args) {
		TestFractal curve1 = new TestFractal(16, Color.BLUE);
		curve1.setVisible(true);
	}

}