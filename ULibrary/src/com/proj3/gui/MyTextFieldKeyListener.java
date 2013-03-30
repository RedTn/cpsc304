package com.proj3.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class MyTextFieldKeyListener implements KeyListener {
	
	public void keyTyped(KeyEvent e) {
		// Do nothing
	}

	public void keyPressed(KeyEvent e) {
		// Do Nothing
	}

	public void keyReleased(KeyEvent e) {
		int cntLimit = ((JTextField)e.getComponent()).getColumns();
		String str = ((JTextField)e.getComponent()).getText();
		int cnt = str.length();
		if (cnt > cntLimit)
			((JTextField)e.getComponent()).setText(str.substring(0, cntLimit));
	}
}
