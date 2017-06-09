import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class SimpleCalculator implements ActionListener {
	JLabel label1 = new JLabel("Number 1:");
	JTextField field1 = new JTextField(10);
	JLabel label2 = new JLabel("Number 2:");
	JTextField field2 = new JTextField(10);
	JLabel label3 = new JLabel("Sum:");
	JTextField result = new JTextField(10);
	JButton go = new JButton("Add");
	
	SimpleCalculator() {
		JPanel windowContent = new JPanel();
		GridLayout g1 = new GridLayout(4, 2);
		windowContent.setLayout(g1);
		
		go.addActionListener(this);
		
		//add controls to the panel
		windowContent.add(label1);
		windowContent.add(field1);
		windowContent.add(label2);
		windowContent.add(field2);
		windowContent.add(label3);
		windowContent.add(result);
		windowContent.add(go);
		
		//create the frame
		JFrame frame = new JFrame("My First Calculator");
		
		frame.setContentPane(windowContent);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set the size and make the window visible
		frame.setSize(400, 100);
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		SimpleCalculator calc = new SimpleCalculator();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int a = Integer.parseInt(field1.getText());
		int b = Integer.parseInt(field2.getText());
		int sum = a+b;
		result.setText(""+sum);
	}
}
