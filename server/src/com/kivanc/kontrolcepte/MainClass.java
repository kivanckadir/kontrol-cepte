package com.kivanc.kontrolcepte;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
 
public class MainClass {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	
	private static Scanner in = null;
	private static String line;
	
	private static boolean isConnected = false;
	private static Robot robot;
	private static int SERVER_PORT = 0;
	private static String nircmdFilePath = "src/com/kivanc/kontrolcepte/nircmd.exe";
	private static String key;
	private static boolean isCDromOpen = false;
	private static MyView myView;
	private static boolean isFirstRun = true;
 
	public static void main(String[] args) {
 
	    try{
			server = new ServerSocket(SERVER_PORT);
			SERVER_PORT = server.getLocalPort();
			
			if(isFirstRun == true) {
				myView = new MyView();
				myView.setVisible(true);
				isFirstRun = false;
			}
			
			myView.mServerIP.setText(Inet4Address.getLocalHost().getHostAddress());
			myView.mPort.setText(String.valueOf(server.getLocalPort()));
			
			
			System.out.println("Server IP: " + Inet4Address.getLocalHost().getHostAddress());
			System.out.println("Port: " + server.getLocalPort());
					

			client = server.accept();
			
			if (client.isConnected()) {
				isConnected=true;
				robot = new Robot();
			}
			
			in = new Scanner (new BufferedReader(new InputStreamReader(client.getInputStream())));
			
		} catch (IOException e) {
			System.out.println("Error while opening socket");
			System.exit(-1);
			
		} catch (AWTException e) {
			System.out.println("Error while creating robot instance");
			System.exit(-1);
		}
	    
		while (isConnected) {
			try {
			line = in.nextLine(); // Read line from client

			System.out.println(line); // Print whatever we get from client

			/*************** Decoder Start ******************/
			if (line.equalsIgnoreCase("mute")) {
				sendKeyPress("mute");
			}

			else if (line.contains("uppercase_")) {
				key = line.split("_")[1];
				sendKeyPress("uppercase_");
			}

			else if (line.contains("lowercase_")) {
				key = line.split("_")[1];
				sendKeyPress("lowercase_");
			}

			else if (line.contains("numeric_")) {
				key = line.split("_")[1];
				sendKeyPress("numeric_");
			}

			else if (line.contains("special_")) {
				key = line.split("_")[1];
				sendKeyPress("special_");
			}

			else if (line.equalsIgnoreCase("volume_up")) {
				sendKeyPress("volume_up");
			}

			else if (line.equalsIgnoreCase("volume_down")) {
				sendKeyPress("volume_down");
			}
			
			else if (line.contains("volume_")) {
				
				setVolume(line.split("_")[1]);
			}

			else if (line.equalsIgnoreCase("stop")) {
				sendKeyPress("stop");
			}

			else if (line.equalsIgnoreCase("previous")) {
				sendKeyPress("previous");
			}

			else if (line.equalsIgnoreCase("play_pause")) {
				sendKeyPress("play_pause");
			}

			else if (line.equalsIgnoreCase("next")) {
				sendKeyPress("next");
			}

			else if (line.contains(",")) {
				double movex = Double.parseDouble(line.split(",")[0]); // extract movement in x direction
				double movey = Double.parseDouble(line.split(",")[1]); // extract movement in y direction

				Point point = MouseInfo.getPointerInfo().getLocation(); // Get current mouse position
				double nowx = point.x;
				double nowy = point.y;

				robot.mouseMove((int) (nowx + movex), (int) (nowy + movey));// Move mouse pointer to new location
			}

			else if (line.contains("left_click_press")) {
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

			}

			else if (line.contains("left_click_release")) {
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}

			else if (line.contains("left_click")) {
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

			}

			else if (line.contains("right_click_press")) {
				robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);

			}

			else if (line.contains("right_click_release")) {
				robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
			}

			else if (line.contains("right_click")) {
				robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
			}
			
			else if (line.contains("shortcut_")) {
				key=line.split("_")[1];
				sendKeyPress(key);
			}
			
			else if (line.contains("eject")) {
				cdrom();
			}

			else if (line.equals("onPause")) {

			}

			else if (line.equals("onDestroy")) {
				isConnected = false;
			}

			else if (line.equals(null)) {

			}}catch (NoSuchElementException e) {
				isConnected = false;
			}
			
			/*************** Decoder End ******************/

		}

		
		 if (!isConnected) {
			 try {
			 System.out.println("Connection Closed");
			 in.close(); robot = null;
			 client.close();
			 server.close();
			 main(args);
			 System.exit(0);
		 	} catch(IOException e) {
		 		
		 	} 
		 }
	}

	public static void sendKeyPress(String msg) {
		Runtime rt = Runtime.getRuntime();
		Process pr;
		String buttonCode = null;
		//boolean isCapsLockClose = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		//System.out.println(isCapsLockClose);
		
		switch (msg) {
		case "uppercase_":
			buttonCode = "shift+"+key;
			break;

		//Case for lowercase characters
		case "lowercase_":
			if(key.equals("i")) {
				key="i";
			}
			buttonCode = key;
			break;
		
		//Case for numeric characters
		case "numeric_":
			buttonCode = key;
			break;
			
		//Case for special characters
		case "special_":
			if(key.equals("space")) {
				key = "spc";
			}
			buttonCode = key;
			break;
			
		case "mute":
			buttonCode = "0xAD";
			break;

		case "volume_down":
			buttonCode = "0xAE";
			break;

		case "volume_up":
			buttonCode = "0xAF";
			break;
			
		case "stop":
			buttonCode = "0xB2";
			break;

		case "previous":
			buttonCode = "0xB1";
			break;

		case "play_pause":
			buttonCode = "0xB3";
			break;

		case "next":
			buttonCode = "0xB0";
			break;
			
		case "mail":
			buttonCode = "0xB4";
			break;
			
		default:
			break;
		}

		try {
			pr = rt.exec(nircmdFilePath + " sendkeypress " + buttonCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setVolume(String msg) {
		Runtime rt = Runtime.getRuntime();
		Process pr;
		int volume = Integer.parseInt(msg);
		volume = volume*65535/100;
		
		try {
			pr = rt.exec(nircmdFilePath + " setvolume 0 " + volume + " " + volume);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void cdrom() {
		Runtime rt = Runtime.getRuntime();
		Process pr;
		
		try {
			if(!isCDromOpen) {
				pr = rt.exec(nircmdFilePath + " cdrom open");
				isCDromOpen=true;
			} else if(isCDromOpen) {
				pr = rt.exec(nircmdFilePath + " cdrom close");
				isCDromOpen=false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}