import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import javax.swing.JFrame;

public class SierpinskiCarpet extends JFrame {

	private Color color;
	
	public static Color[] colors = { 
		Color.BLUE,
		Color.GREEN,
		Color.RED,
		Color.ORANGE
	};

	public SierpinskiCarpet(int iter) {
		super("Sierpinski Carpet");
		setBounds(100, 100, 700, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		color = Color.BLACK;

	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	* Decides if a point at a specific location is filled or not.  This works by iteration first checking if
	* the pixel is unfilled in successively larger squares or cannot be in the center of any larger square.
	* @param x is the x coordinate of the point being checked with zero being the first pixel
	* @param y is the y coordinate of the point being checked with zero being the first pixel
	* @return 1 if it is to be filled or 0 if it is open
	*/
	public boolean isSierpinskiCarpetPixelFilled(int x, int y)
	{
	    while(x>0 || y>0) // when either of these reaches zero the pixel is determined to be on the edge 
	                               // at that square level and must be filled
	    {
	        if(x%3==1 && y%3==1) //checks if the pixel is in the center for the current square level
	            return false;
	        x /= 3; //x and y are decremented to check the next larger square level
	        y /= 3;
	    }
	    return true; // if all possible square levels are checked and the pixel is not determined 
	                   // to be open it must be filled
	}	

	@Override
	public void paint(Graphics g) {
		int x0 = 50;
		int y0 = 50;
		
		g.setColor(color);
		for(int x = 0; x < 675; x++) {
			for(int y = 0; y < 675; y++) {
				if(isSierpinskiCarpetPixelFilled(x, y)) {
					g.drawLine(x+x0, y+y0, x+x0, y+y0);
				}
			}
		}
		
	}

	public static void main(String[] args) {
		SierpinskiCarpet curve1 = new SierpinskiCarpet(16);
		curve1.setVisible(true);
	}

}