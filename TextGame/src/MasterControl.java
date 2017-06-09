import javax.swing.*;
import java.awt.*;

public class MasterControl {
	JTextArea a1 = new JTextArea();
	Graphics2D g1;
	
	TextMap world = new TextMap(20);
	Adventurer adv_1 = new Adventurer('V');
	Adventurer adv_2 = new Adventurer('G');
	
	MasterControl() {
		JPanel windowContent = new JPanel();
		FlowLayout f1 = new FlowLayout();
		windowContent.setLayout(f1);
		windowContent.add(a1);
		JFrame frame = new JFrame("Master Control Test");
		frame.setContentPane(windowContent);
		frame.setSize(10*world.mapsize, 20*world.mapsize);
		frame.setVisible(true);
	}

	public void run() {
		world.initializeFlatMap();
		world.map[adv_1.x][adv_1.y] = adv_1.avatar;
		world.map[adv_2.x][adv_2.y] = adv_2.avatar;
		a1.replaceRange(world.drawMap(), 0, a1.getText().length());
		/*
		while(true) {
			world.map[adv_1.x][adv_1.y] = adv_1.avatar;
			world.map[adv_2.x][adv_2.y] = adv_2.avatar;
			a1.replaceRange(world.drawMap(), 0, a1.getText().length());
			
			world.map[adv_1.x][adv_1.y] = '+';
			world.map[adv_2.x][adv_2.y] = '+';
		}  
		*/
	}
	public static void main(String[] args) {
		MasterControl test = new MasterControl();
		test.run();
	}
}
