package com.proj3.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import com.proj3.app.BorrowerApp;
import com.proj3.app.ClerkApp;
import com.proj3.app.LibrarianApp;
import com.proj3.database.Database;

@SuppressWarnings("serial")
public class MainJFrame extends JFrame {

	public final static int FRAME_WIDTH = 700;
	public final static int FRAME_HEIGHT = 500;
	public final static int ICON_WIDTH = 100;
	public final static int ICON_HEIGHT = 100;
	public final static int LOG_WIDTH = 0;
	public final static int LOG_HEIGHT = 4;

	private BorrowerUILogin borrowerUILogin;

	private JFrame frame;

	private JTextArea errorLog, outputLog;

	private Container defaultPane;

	private Boolean isConnected = false;

	private BorrowerApp bApp;
	private LibrarianApp lApp;
	private ClerkApp cApp;
	private Database db;

	public MainJFrame(BorrowerApp b, LibrarianApp l, ClerkApp c, Database d) {
		bApp = b;
		lApp = l;
		cApp = c;
		db = d;
	}

	public Database getDB() {
		return db;
	}

	public String getCurrentUserBID() {
		int bid = bApp.getBID();
		if (bid < 0) {
			return "Anonymous";
		}
		return String.valueOf(bid);
	}

	private ImageIcon createImageIcon(String imagePath) {

		if (imagePath == null)
			return null;

		java.net.URL imageURL = MainJFrame.class.getResource(imagePath);

		if (imageURL == null)
			return null;

		ImageIcon icon = new ImageIcon(imageURL);
		Image img = icon.getImage();
		Image scaledImg = img.getScaledInstance(ICON_WIDTH, ICON_HEIGHT,
				java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImg);

	}

	/**
	 * Adds help menu to the menu bar
	 * 
	 * @param jMenuBar
	 *            Menu bar to contain the menu
	 */
	private JMenu addHelpMenu() {

		JMenu menu;
		JMenuItem menuItem;
		ImageIcon icon;

		icon = createImageIcon("/help.png");
		menu = new JMenu("");
		menu.setIcon(icon);

		icon = createImageIcon("/connect.png");
		menuItem = new JMenuItem("Connection");
		menuItem.setIcon(icon);

		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(defaultPane,
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});

		menu.add(menuItem);

		icon = createImageIcon("/helpbook.png");
		menuItem = new JMenuItem("Manual");
		menuItem.setIcon(icon);
		menu.add(menuItem);

		return menu;

	}

	/**
	 * Adds librarian menu and menu items to the menu bar
	 * 
	 * @param jMenuBar
	 *            Menu bar to contain the menu
	 */
	private JMenu addLibrarianMenu() {

		JMenu menu, subMenu;
		JMenuItem menuItem;
		ImageIcon icon;

		icon = createImageIcon("/librarianFace.png");
		menu = new JMenu("Librarian");
		menu.setIcon(icon);

		icon = createImageIcon("/add.png");
		subMenu = new JMenu("Add");
		subMenu.setIcon(icon);

		icon = createImageIcon("/newbook.png");
		menuItem = new JMenuItem("New Book", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new LibrarianUIAddBook(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		subMenu.add(menuItem);

		icon = createImageIcon("/newcopy.png");
		menuItem = new JMenuItem("New Copy", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new LibrarianUIAddCopy(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		subMenu.add(menuItem);

		menu.add(subMenu);

		icon = createImageIcon("/reports.png");
		subMenu = new JMenu("Generate Reports");
		subMenu.setIcon(icon);

		icon = createImageIcon("/checkedoutbooks.png");
		menuItem = new JMenuItem("Checked-Out Books", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new LibrarianUIGenerateBook(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		subMenu.add(menuItem);

		icon = createImageIcon("/mostpopularitems.png");
		menuItem = new JMenuItem("Most Popular Items", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new LibrarianUIGenerateItem(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		subMenu.add(menuItem);

		menu.add(subMenu);

		return menu;
	}

	/**
	 * Adds borrower menu and menu items to the menu bar
	 * 
	 * @param menuBar
	 */
	private JMenu addBorrowerMenu() {

		JMenu menu;
		JMenuItem menuItem;
		ImageIcon icon;

		icon = createImageIcon("/borrowerFace.png");
		menu = new JMenu("Borrower");
		menu.setIcon(icon);

		icon = createImageIcon("/login.png");
		menuItem = new JMenuItem("Log In", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(borrowerUILogin,
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
				
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/search.png");
		menuItem = new JMenuItem("Search Books", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new BorrowerUISearch(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/account.png");
		menuItem = new JMenuItem("Check Account", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				BorrowerUICheck checkPanel = new BorrowerUICheck(getMainFrame());
				getFrame().getContentPane().add(
						checkPanel,
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
				
				checkPanel.populateInfo();
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/hold.png");
		menuItem = new JMenuItem("Place Hold Request", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane()
						.add(new BorrowerUIHold(getMainFrame()),
								BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/fine.png");
		menuItem = new JMenuItem("Pay Fine", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				
				BorrowerUIFine finePanel = new BorrowerUIFine(getMainFrame());
				getFrame().getContentPane().add(finePanel,BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();

				finePanel.populateInfo();
			}

		});
		menu.add(menuItem);
		menu.add(menuItem);

		return menu;
	}

	/**
	 * Adds clerk menu and menu items to the menu bar
	 * 
	 * @param jMenuBar
	 *            Menu bar to contain the menu
	 */
	private JMenu addClerkMenu() {

		JMenu menu;
		JMenuItem menuItem;
		ImageIcon icon;

		icon = createImageIcon("/clerkFace.png");
		menu = new JMenu("Clerk");
		menu.setIcon(icon);
		menu.setMnemonic(KeyEvent.VK_ALT);

		icon = createImageIcon("/borrower.png");
		menuItem = new JMenuItem("Add New Borrower", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new ClerkUIAddBorrower(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/checkout.png");
		menuItem = new JMenuItem("Check-Out Item", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new ClerkUICheckOut(getMainFrame()),
						BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/return.png");
		menuItem = new JMenuItem("Process Return", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane().add(
						new ClerkUIReturn(getMainFrame()), BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		menu.add(menuItem);

		icon = createImageIcon("/overdue.png");
		menuItem = new JMenuItem("Check Overdue Items", icon);
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int cnt = getFrame().getContentPane().getComponentCount();
				for (int i = 1; i < cnt; i++)
					getFrame().getContentPane().remove(i);
				getFrame().getContentPane()
						.add(new ClerkUIOverdue(getMainFrame()),
								BorderLayout.CENTER);
				getFrame().validate();
				getFrame().repaint();
			}

		});
		menu.add(menuItem);

		return menu;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar;
		menuBar = new JMenuBar();

		return menuBar;
	}

	private Container createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);

		return contentPane;
	}

	private JPanel createLogPane() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		GridBagConstraints gridc;

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 0;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(createErrorScrollPane(), gridc);

		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.gridx = 1;
		gridc.gridy = 0;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(createOutputScrollPane(), gridc);

		return panel;
	}

	private JScrollPane createErrorScrollPane() {
		errorLog = new JTextArea(LOG_HEIGHT, LOG_WIDTH);
		errorLog.setEditable(false);
		errorLog.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(errorLog);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Error Log"));
		return scrollPane;
	}

	private JScrollPane createOutputScrollPane() {
		outputLog = new JTextArea(LOG_HEIGHT, LOG_WIDTH);
		outputLog.setEditable(false);
		outputLog.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(outputLog);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Output Log"));
		return scrollPane;
	}

	private Container createDefaultPanel() {

		JButton button = new JButton();
		button.setText("Welcome to [ULibrary DBMS]  " + "Click To Connect");
		button.setFont(new Font("SansSerif", Font.BOLD, 20));
		button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isConnected == false) {
					try {
						isConnected = db.connect();
						if (isConnected == true)
							((JButton) e.getSource()).setText("Connected!!  "
									+ "Click to Disconnect");
						else
							throw new RuntimeException("Failed Connection");
					} catch (Exception ex) {
						((JButton) e.getSource())
								.setText("Connection Unsuccessful.  "
										+ "Try Again by Clicking this Button");
					}
				}
			}

		});
		JPanel panel = new JPanel();
		panel.add(Box.createVerticalStrut(FRAME_HEIGHT / 2));
		panel.add(button, new BorderLayout());

		return panel;
	}

	private JFrame getFrame() {
		return frame;
	}

	private MainJFrame getMainFrame() {
		return this;
	}

	void displayErrorMessage(String s) {

		errorLog.setText(s);
		errorLog.validate();
		errorLog.repaint();

	}

	public void displayOutputMessage(String s) {

		outputLog.append(s + "\n");
		outputLog.validate();
		outputLog.repaint();

	}

	public void createAndShowGUI() {

		frame = new JFrame("UBC CPSC 304 LIBRARY");
		frame.setIconImage(createImageIcon("/library.png").getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = createMenuBar();
		menuBar.add(addClerkMenu());
		menuBar.add(addBorrowerMenu());
		menuBar.add(addLibrarianMenu());
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(addHelpMenu());

		borrowerUILogin = new BorrowerUILogin(getMainFrame());

		Container contentPane = this.createContentPane();
		defaultPane = createDefaultPanel();
		contentPane.add(createLogPane(), BorderLayout.PAGE_END);
		contentPane.add(defaultPane, BorderLayout.CENTER);

		frame.setJMenuBar(menuBar);
		frame.setContentPane(contentPane);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setResizable(true);
		frame.setVisible(true);

		EventQueue systemEventQueue = Toolkit.getDefaultToolkit()
				.getSystemEventQueue();
		systemEventQueue.push(new MyEventQueue());

	}

	public BorrowerApp bApp() {
		return bApp;
	}

	public LibrarianApp lApp() {
		return lApp;
	}

	public ClerkApp cApp() {
		return cApp;
	}

	// EventQueue to handle all runtime exceptions thrown by Swing componenets
	class MyEventQueue extends EventQueue {

		@Override
		protected void dispatchEvent(AWTEvent newEvent) {
			try {
				super.dispatchEvent(newEvent);
			} catch (Throwable t) {
				displayErrorMessage(t.getMessage());
			}
		}
	}

}
