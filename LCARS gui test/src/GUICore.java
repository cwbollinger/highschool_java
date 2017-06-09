import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class GUICore extends JFrame {
	
	LCARSComponent btn1;
	LCARSComponent btn2;
	
	GridBagConstraints gbc;
	GridBagLayout gbl;

	public GUICore() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(200,200));
		gbl = new GridBagLayout();
		this.getContentPane().setLayout(gbl);
		gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		btn1 = new LCARSComponent(70, 50, "btn 1", LCARSComponent.Type.BUTTON);
		btn1.getButton().setBackground(Color.lightGray);
		this.getContentPane().add(btn1.getButton(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		btn2 = new LCARSComponent(70, 50, "btn 2", LCARSComponent.Type.BUTTON);
		btn2.getButton().setBackground(Color.lightGray);
		this.getContentPane().add(btn2.getButton(), gbc);

		pack();
		setVisible(true);

	}

	public static void main(String[] args) {
		GUICore test = new GUICore();
		
	}
}
