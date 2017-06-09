/*
 ****************************************************************
 ****************************************************************
 ******* 													*****
 ******* 	PROGRAMMER: JUNALD ASTRONOMO LAGOD				*****
 ******* 	CONTACT NUMBER: +639195671599					*****
 ******* 	E-MAIL ADDRESS: JUNALDLAGOD@YAHOO.COM			*****
 ******* 	WEBSITE: HTTP://WWW.JUNALDLAGOD.CJB.NET			*****
 ******* 													*****
 *******	MABUHAY ANG MGA PINOY							*****
 *******	PLEASE DON'T FORGET TO VOTE"    				*****
 ******* 													*****
 ****************************************************************
 ****************************************************************
 */

import javax.swing.*;
import java.awt.*;

public class frmSplash extends JWindow implements Runnable{
	public void run(){
		JLabel SplashLabel = new JLabel(new ImageIcon("@imgs/Splash.jpg"));
		Dimension screen = 	Toolkit.getDefaultToolkit().getScreenSize();
		
		getContentPane().add(SplashLabel,BorderLayout.CENTER);
		
		setSize(490,300);
		setLocation((screen.width - 490)/2,((screen.height-300)/2));
		show();
	}
}