package com.proj3.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.proj3.app.BorrowerApp;

@SuppressWarnings("serial")
public class BorrowerPanel extends JPanel {
	
	private MainJFrame mainFrame;
	
	public JPanel getThisPanel() {
		return this;
	}

	protected BorrowerApp bApp() {
		return mainFrame.bApp();
	}
	
	protected void setMainFrame(MainJFrame m) {
		mainFrame = m;
	}
	
	public void setBorderRed(JTextField field, Boolean b) {
		if (b)
			field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.pink));
		else
			field.setBorder(BorderFactory.createEtchedBorder());
	}
	
	public String getCurrentUserBID() {
		return mainFrame.getCurrentUserBID();
	}
	
	public void displayOutput(String str) {
		mainFrame.displayOutputMessage(str);
	}
	
	public void displayErrorMessage(String str) {
		mainFrame.displayErrorMessage(str);
	}
	
	protected void addProgressBar(JProgressBar progressBar) {
		progressBar.setIndeterminate(true);
		GridBagConstraints gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = GridBagConstraints.PAGE_END;
		gridc.weightx = 1;
		gridc.gridwidth = GridBagConstraints.REMAINDER;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		getThisPanel().add(progressBar, gridc);
		getThisPanel().validate();
		getThisPanel().repaint();
	}
	
	// ActionListener for enforcing not null constraint
	class MyFocusListener implements FocusListener {

		public void focusGained(FocusEvent e) {
			// Do Nothing
		}

		public void focusLost(FocusEvent e) {	
			if (((JTextField)e.getComponent()).getText()==null || ((JTextField)e.getComponent()).getText().isEmpty()) {
				setBorderRed(((JTextField)e.getComponent()), true);
				throw new NullPointerException(((JTextField)e.getComponent()).getName() + " can not be null.");
			}
			setBorderRed(((JTextField)e.getComponent()), false);
		}

	}
}
