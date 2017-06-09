import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LayoutTest extends JFrame {

  protected CardLayout layout;

  public static void main(String[] args) {
    LayoutTest ct = new LayoutTest();
    ct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ct.displayTab("Green Tab");
    ct.setSize(400, 300);
    ct.setVisible(true);
  }

  public LayoutTest() {
    BasicCard tab1;
    Container pane = getContentPane();
    layout = new CardLayout();
    pane.setLayout(layout);
    tab1 = new BasicCard("Card 1");
    tab1.setBackground(Color.red);
    pane.add(tab1, "Red Tab");
    BasicCard tab2;
    tab2 = new BasicCard("Card 2");
    tab2.setBackground(Color.green);
    pane.add(tab2, "Green Tab");
    BasicCard tab3;
    tab3 = new BasicCard("Card 2");
    tab3.setBackground(Color.blue);
    pane.add(tab3, "Blue Tab");
  }
  
  public void addTab(BasicCard card) {
	  getContentPane().add(card, card.name);
  }

  public void displayTab(String name) {
	
    layout.show(this.getContentPane(), name);
  }

}