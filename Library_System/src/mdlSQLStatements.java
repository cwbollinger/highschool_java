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
 *******	PLEASE DON'T FORGET TO VOTE"    				*****			*****
 ******* 													*****
 ****************************************************************
 ****************************************************************
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.sql.*;

 public class mdlSQLStatements
 {
 	//This function is to remove item in the records
	public void recREMOVE(boolean sSTATUS, Statement stmt, String sTable, String sField, JTable JTBrrwrsTbl, int sNum) throws SQLException
	{
		if (true)
		{
			stmt.execute("DELETE FROM " + sTable + " WHERE " + sField + " ='" + JTBrrwrsTbl.getValueAt(JTBrrwrsTbl.getSelectedRow(),sNum) + "'");
		}
		else
		{
			stmt.execute("DELETE FROM " + sTable + " WHERE " + sField + " =" + JTBrrwrsTbl.getValueAt(JTBrrwrsTbl.getSelectedRow(),sNum));
		}
	}
 }