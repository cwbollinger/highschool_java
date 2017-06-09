import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BasicCard extends JPanel implements ActionListener {
	
	private CardLayout deck;
	
	String name;
	
	JButton PrevCard;
	JButton NextCard;

	public BasicCard(String name) {
		this.name = name;
		PrevCard = new JButton("Previous Card");
		NextCard = new JButton("Next Card");
		add(PrevCard);
		add(NextCard);
		setFocusable(true);
		requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if(button.getText().equals("Previous Card")) {
			System.out.println("Prev Card");
			deck.previous(this.getParent());
		} else if(button.getText().equals("Next Card")) {
			System.out.println("Next Card");
			deck.next(this.getParent());
		}
		
	}

}
