import java.awt.*;
import javax.swing.*;

public class HeistCore extends JFrame {
	
	public static final boolean DEBUG = true;

	
	public static HeistCore mainClass;
	public static HeistPanel heist;
	public static MenuPanel menu;
	public LevelReader loader;

	public HeistCore() {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("THE HEIST!");
		setLocation(0, 0);
		heist = new HeistPanel();
		heist.setPreferredSize(new Dimension(heist.width, heist.height));
		menu = new MenuPanel();
		menu.setPreferredSize(new Dimension(menu.width, menu.height));
		getContentPane().add(menu);
		
		pack();
		
		setVisible(true);
	}

	public static void main(String[] args) {
		mainClass = new HeistCore();
		
		if(DEBUG) {
			new DebugWindow();
		}
	}

}