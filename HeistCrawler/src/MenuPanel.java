import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import javax.swing.*;

public class MenuPanel extends JPanel implements ActionListener {

	public static MenuPanel mainPanel;

	JButton quit;
	JButton play;

	public static int width = 800;
	public static int height = 800;

	Image backgroundImage;

	public MenuPanel() {
		mainPanel = this;

		quit = new JButton("quit");
		quit.addActionListener(this);
		play = new JButton("play");
		play.addActionListener(this);
		this.add(quit);
		this.add(play);

		setFocusable(true);
		requestFocus();

		backgroundImage = Toolkit.getDefaultToolkit().createImage(
				"Images/MenuBack.png");

		prepareImage(backgroundImage, this);
	}

	public void paintComponent(Graphics g) {
		draw(g);
	}

	public void draw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String name = button.getText();

		if (name.equals("quit")) {
			System.exit(JFrame.EXIT_ON_CLOSE);
		} else if (name.equals("play")) {
			HeistCore.mainClass.getContentPane().removeAll();
			HeistCore.mainClass.getContentPane().add(HeistCore.heist);
		}

	}

}