import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class MandelbrotFractal extends JFrame {
	
	private int max_iteration;
	private boolean colored;

	public MandelbrotFractal(int iter) {
		super("Mandelbrot Set");
		max_iteration = iter;
		setBounds(0, 0, 1200, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public MandelbrotFractal(int iter, boolean colored) {
		super("Mandelbrot Set");
		max_iteration = iter;
		this.colored = colored;
		setBounds(0, 0, 1200, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	// x0 = scaled x coordinate of pixel (must be scaled to lie somewhere in
	// the mandelbrot X scale (-2.5, 1)
	// y0 = scaled y coordinate of pixel (must be scaled to lie somewhere in
	// the mandelbrot Y scale (-1, 1)
	public Color getPixelColor(double x0, double y0) {

		double x = 0.0;
		double y = 0.0;
		double xtemp = 0.0;

		int iteration = 0;
		while (x * x + y * y < 2 * 2 && iteration < max_iteration) {

			xtemp = x * x - y * y + x0;
			y = 2 * x * y + y0;
			x = xtemp;

			iteration++;
		}

		if(!colored) {
			if(iteration == max_iteration) {
				return Color.black;
			} else {
				return Color.white;
			}
		}
		
		if (iteration == max_iteration) {
			return Color.black;
		} else if(iteration > max_iteration*9.0/500.0){
			return Color.darkGray;
		} else if(iteration > max_iteration*8.0/500.0){
			return Color.blue;
		} else if(iteration > max_iteration*7.0/500.0){
			return Color.green;
		} else if(iteration > max_iteration*6.0/500.0){
			return Color.orange;
		} else if(iteration > max_iteration*5.0/500.0){
			return Color.red;
		} else if(iteration > max_iteration*4.0/500.0){
			return Color.yellow;
		} else if(iteration > max_iteration*3.0/500.0){
			return Color.pink;
		} else if(iteration > max_iteration*2.0/500.0){
			return Color.cyan;
		} else if(iteration > max_iteration/500.0){
			return Color.lightGray;
		} else {
			return Color.white;
		}

	}

	@Override
	public void paint(Graphics g) {
		int screen_X;
		int screen_Y;
		for (double y = -2; y < 2; y += 0.001) {
			for (double x = -2.5; x < 2; x += 0.001) {
				Color color = getPixelColor(x, y);
				screen_X = (int)(Math.round(x*300)+800);
				screen_Y = (int)(Math.round(y*300)+400);
				g.setColor(color);
				g.drawLine(screen_X, screen_Y, screen_X, screen_Y);
			}
		}
	}

	public static void main(String[] args) {
		MandelbrotFractal test = new MandelbrotFractal(1000, false);
		test.setVisible(true);
	}
}
