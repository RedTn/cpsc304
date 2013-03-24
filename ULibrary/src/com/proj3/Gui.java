//src --> pacakge "com" --> folder "proj3"
package com.proj3;

import javax.swing.JFrame;

public class Gui extends JFrame {
	public static void main (String args[]) {
		Gui mygui = new Gui();
		mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mygui.setSize(200, 200);
		mygui.setVisible(true);
		mygui.setTitle("Test");
	}
}