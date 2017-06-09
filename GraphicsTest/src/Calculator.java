import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator implements ActionListener {
	// Declaration of all calculator's components.
	private static final char NO_OPERATOR = '_';

	JPanel windowContent;
	JTextField displayField;
	JButton button0;
	JButton button1;
	JButton button2;
	JButton button3;
	JButton button4;
	JButton button5;
	JButton button6;
	JButton button7;
	JButton button8;
	JButton button9;
	JButton buttonPoint;
	JButton buttonEqual;
	JButton buttonAdd;
	JButton buttonSubtract;
	JButton buttonMultiply;
	JButton buttonDivide;
	JButton buttonSqrt;
	JButton buttonPow;
	JButton buttonClear;
	JButton buttonMemRecall;
	JButton buttonMemSave;
	JButton buttonMemClear;
	
	JPanel p1;

	StringBuilder displayText;
	double num1, num2;
	double saveVal;
	double result;
	char operator;
	boolean decimalEntered;
	boolean firstNum;

	char lastChar;

	// Constructor
	Calculator() {
		windowContent = new JPanel();

		BorderLayout b1 = new BorderLayout();
		windowContent.setLayout(b1);

		displayField = new JTextField(20);
		displayField.setEditable(false);
		displayField.setBackground(Color.WHITE);

		displayText = new StringBuilder();

		decimalEntered = false;
		firstNum = true;
		operator = NO_OPERATOR;
		num1 = num2 = 0.0;
		saveVal = 0.0;

		lastChar = 'a';

		windowContent.add("North", displayField);

		button0 = new JButton("0");
		button0.setBackground(Color.BLACK);
		button0.setForeground(Color.WHITE);
		button0.addActionListener(this);
		button1 = new JButton("1");
		button1.setBackground(Color.BLACK);
		button1.setForeground(Color.WHITE);
		button1.addActionListener(this);
		button2 = new JButton("2");
		button2.setBackground(Color.BLACK);
		button2.setForeground(Color.WHITE);
		button2.addActionListener(this);
		button3 = new JButton("3");
		button3.setBackground(Color.BLACK);
		button3.setForeground(Color.WHITE);
		button3.addActionListener(this);
		button4 = new JButton("4");
		button4.setBackground(Color.BLACK);
		button4.setForeground(Color.WHITE);
		button4.addActionListener(this);
		button5 = new JButton("5");
		button5.setBackground(Color.BLACK);
		button5.setForeground(Color.WHITE);
		button5.addActionListener(this);
		button6 = new JButton("6");
		button6.setBackground(Color.BLACK);
		button6.setForeground(Color.WHITE);
		button6.addActionListener(this);
		button7 = new JButton("7");
		button7.setBackground(Color.BLACK);
		button7.setForeground(Color.WHITE);
		button7.addActionListener(this);
		button8 = new JButton("8");
		button8.setBackground(Color.BLACK);
		button8.setForeground(Color.WHITE);
		button8.addActionListener(this);
		button9 = new JButton("9");
		button9.setBackground(Color.BLACK);
		button9.setForeground(Color.WHITE);
		button9.addActionListener(this);
		buttonPoint = new JButton(".");
		buttonPoint.setBackground(Color.BLACK);
		buttonPoint.setForeground(Color.WHITE);
		buttonPoint.addActionListener(this);
		buttonEqual = new JButton("=");
		buttonEqual.setBackground(Color.BLACK);
		buttonEqual.setForeground(Color.WHITE);
		buttonEqual.addActionListener(this);
		buttonAdd = new JButton("+");
		buttonAdd.setBackground(Color.BLACK);
		buttonAdd.setForeground(Color.WHITE);
		buttonAdd.addActionListener(this);
		buttonSubtract = new JButton("-");
		buttonSubtract.setBackground(Color.BLACK);
		buttonSubtract.setForeground(Color.WHITE);
		buttonSubtract.addActionListener(this);
		buttonMultiply = new JButton("*");
		buttonMultiply.setBackground(Color.BLACK);
		buttonMultiply.setForeground(Color.WHITE);
		buttonMultiply.addActionListener(this);
		buttonDivide = new JButton("/");
		buttonDivide.setBackground(Color.BLACK);
		buttonDivide.setForeground(Color.WHITE);
		buttonDivide.addActionListener(this);
		buttonPow = new JButton("^");
		buttonPow.setBackground(Color.BLACK);
		buttonPow.setForeground(Color.WHITE);
		buttonPow.addActionListener(this);
		buttonSqrt = new JButton("/u221A");
		buttonSqrt.setBackground(Color.BLACK);
		buttonSqrt.setForeground(Color.WHITE);
		buttonSqrt.addActionListener(this);
		buttonClear = new JButton("C");
		buttonClear.setBackground(Color.RED);
		buttonClear.setForeground(Color.WHITE);
		buttonClear.addActionListener(this);
		buttonMemRecall = new JButton("MR");
		buttonMemRecall.setBackground(Color.BLACK);
		buttonMemRecall.setForeground(Color.WHITE);
		buttonMemRecall.addActionListener(this);
		buttonMemSave = new JButton ("MS");
		buttonMemSave.setBackground(Color.BLACK);
		buttonMemSave.setForeground(Color.WHITE);
		buttonMemSave.addActionListener(this);
		buttonMemClear = new JButton("MC");
		buttonMemClear.setBackground(Color.BLUE);
		buttonMemClear.setForeground(Color.WHITE);
		buttonMemClear.addActionListener(this);

		p1 = new JPanel();
		p1.setBackground(Color.GRAY);
		// GridLayout g1 = new GridLayout(4,4);
		GridBagLayout gbl = new GridBagLayout();
		p1.setLayout(gbl);
		GridBagConstraints c = new GridBagConstraints();

		// Add window controls to p1
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button7, c);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button8, c);
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button9, c);
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonAdd, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button4, c);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button5, c);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button6, c);
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonSubtract, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button1, c);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button2, c);
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(button3, c);
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonMultiply, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		p1.add(button0, c);
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonPoint, c);
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonDivide, c);
		c.gridx = 5;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonPow, c);
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonSqrt, c);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 2;
		p1.add(buttonEqual, c);
		c.gridx = 6;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonClear, c);
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		p1.add(buttonMemClear, c);
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonMemSave, c);
		c.gridx = 6;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		p1.add(buttonMemRecall, c);

		windowContent.add("Center", p1);

		JFrame frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(windowContent);
		frame.setResizable(false);
		frame.setLocation(100, 100);
		frame.setBackground(Color.BLACK);
		
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		char c = b.getText().charAt(0);
		if (b.getText().equals("C")) {
			clear();
		} else if (b.getText().equals("MC")) {
			saveVal = 0.0;
		} else if (b.getText().equals("MR")) {
			displayText.delete(0, displayText.length());
			displayText.append(saveVal);
		} else if (b.getText().equals("MS")) {
			saveVal = result;
		}else if (isDigit(c)) {
			displayText.append(c);
		} else if (c == '.') {
			if (!decimalEntered) {
				displayText.append(c);
				decimalEntered = true;
			}
		} else {
			if (firstNum) {
				num1 = Double.valueOf(displayText.toString());
				result = num1;
				firstNum = false;
			} else if (lastChar == '=') {

			} else {
				num2 = Double.valueOf(displayText.toString());
			}
			displayText.delete(0, displayText.length());
			decimalEntered = false;

			if (operator == '+') {
				result = num1 + num2;
			} else if (operator == '-') {
				result = num1 - num2;
			} else if (operator == '*') {
				result = num1 * num2;
			} else if (operator == '/') {
				result = num1 / num2;
			}

			if (c == '=') {
				displayText.delete(0, displayText.length());
				displayText.append(result);
				num1 = result;
			} else if (isOperator(c)) {
				operator = c;

			}
		}

		lastChar = c;
		displayField.setText(displayText.toString());

	}

	public boolean isDigit(char c) {
		if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4'
				|| c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOperator(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/') {
			return true;
		} else {
			return false;
		}
	}

	public void clear() {
		num1 = 0.0;
		num2 = 0.0;
		result = 0.0;
		operator = NO_OPERATOR;
		firstNum = true;
		decimalEntered = false;
		displayText.delete(0, displayText.length());
		lastChar = '0';
	}

	public static void main(String[] args) {
		Calculator calc = new Calculator();
	}
}