package org.lejos.pcexample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

/**
 * This is a PC sample. It connects to the NXT, and then sends a command and
 * waits for a reply.
 * 
 * Your NXT must be running a program to receive cmds and respond to them.
 * 
 */
public class BTcommand {
	public static void main(String[] args) {
		NXTConnector conn = new NXTConnector();

		conn.addLogListener(new NXTCommLogListener() {

			public void logEvent(String message) {
				System.out.println("BTSend Log.listener: " + message);
			}

			public void logEvent(Throwable throwable) {
				System.out.println("BTSend Log.listener - stack trace: ");
				throwable.printStackTrace();

			}

		});
		// Connect to any NXT over Bluetooth
		boolean connected = conn.connectTo("btspp://NXTChris");

		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}

		// Setup Bluetooth I/O
		DataOutputStream btout = new DataOutputStream(conn.getOutputStream());
		DataInputStream btin = new DataInputStream(conn.getInputStream());

		String cmd;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		
		while (true) {

			try {
				cmd = stdin.readLine();
			} catch (IOException e) {
				// bail out
				e.printStackTrace();
				cmd = null;
			}
			
			if (cmd == null) {
				break;
			}

			try {
				System.out.println("Sending " + cmd);
				btout.writeUTF(cmd);
				btout.flush();

			} catch (IOException ioe) {
				System.err.println("IO Exception writing bytes:");
				System.err.println(ioe.getMessage());
				break;
			}

			try {
				System.out.println("Received " + btin.readInt());
			} catch (IOException ioe) {
				System.err.println("IOException reading bytes:");
				System.err.println(ioe.getMessage());
				break;
			}
		}

		// clean up BT connection
		try {
			btin.close();
			btout.close();
			conn.close();
		} catch (IOException ioe) {
			System.err.println("IOException closing connection:");
			System.err.println(ioe.getMessage());
		}
	}
}
