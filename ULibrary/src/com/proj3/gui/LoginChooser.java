package com.proj3.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginChooser extends JPanel{
	MainScreen container; 
	final String[] options = {"Borrower", "Clerk", "Librarian"};
	final JComboBox typeList = new JComboBox(options);
	
	public LoginChooser(MainScreen c) {
		container = c;
		
		JButton goButton = new JButton("Go");
		JLabel label = new JLabel("Login as:");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel bpanel = new JPanel();
		bpanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		bpanel.add(typeList);
		bpanel.add(goButton);
		
		add(label);
		add(bpanel);
		
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = typeList.getSelectedIndex();
				
				switch (selected) {
				case 0:
					container.showBorrowerScreen();
					break;
				case 1:
					container.showClerkScreen();
					break;
				case 2:
					container.showLibrarianScreen();
				default:
					break;
				}
				
			}
		});
	}
}
