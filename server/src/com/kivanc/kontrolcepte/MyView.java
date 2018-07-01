package com.kivanc.kontrolcepte;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class MyView extends JFrame {
		
	JPanel mContainPanel = new JPanel(new SpringLayout());
	//JPanel mStatusPanel = new JPanel(new SpringLayout());
	
	JLabel lblServerIP = new JLabel("Server IP: ", SwingConstants.RIGHT);
	JLabel lblPort = new JLabel("Port: ", SwingConstants.RIGHT);
	JLabel mServerIP = new JLabel();
	JLabel mPort = new JLabel();
		
	public MyView() {
		super("Kontrol Cepte");

		/*setMinimumSize(new Dimension(330, 110));
		setPreferredSize(new Dimension(330, 110));
		setMaximumSize(new Dimension(330, 110));*/
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		getContentPane().setBackground(Color.WHITE);
		
		lblServerIP.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblPort.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		mServerIP.setFont(new Font("Segoe UI", Font.BOLD, 18));
		mPort.setFont(new Font("Segoe UI", Font.BOLD, 18));


		mContainPanel.setBackground(Color.WHITE);
		mContainPanel.add(lblServerIP);
		mContainPanel.add(mServerIP);
		mContainPanel.add(lblPort);
		mContainPanel.add(mPort);
		
		//mStatusPanel.setBackground(Color.RED);
		//mStatusPanel.setMaximumSize(new Dimension(50, 50));
		
		add(mContainPanel);
		//add(mStatusPanel);

		SpringUtilities.makeCompactGrid(mContainPanel, 2, 2, 5, 5, 5, 5);
	}
}
