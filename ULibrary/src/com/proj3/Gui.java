//src --> pacakge "com" --> folder "proj3"
package com.proj3;

//Gui package
import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
	public static void main (String args[]) {
		Gui mygui = new Gui();
		
		//CLose on "X"
		mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mygui.setSize(500, 300);
		mygui.setVisible(true);
		mygui.setTitle("Main Menu");
	}
}