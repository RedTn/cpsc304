package com.proj3.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.proj3.model.Book;
import com.proj3.model.CopyStatus;

@SuppressWarnings("serial")
public class BorrowerUISearch extends BorrowerPanel implements ActionListener {

	private static final String TITLE_STRING = "TITLE";
	private static final String AUTHOR_STRING = "AUTHOR";
	private static final String SUBJECT_STRING = "SUBJECT";
	private static final String ITEMLIST_STRING = "ITEMS";
	private static final String COPYIN_STRING = "IN";
	private static final String COPYOUT_STRING = "OUT";
	
	private JTextField titleField, authorField, subjectField;

	private JLabel titleFieldLabel, authorFieldLabel, subjectFieldLabel;

	private JTextArea itemListArea, copyInArea, copyOutArea;
	private JScrollPane itemListPane, copyInPane, copyOutPane;
	
	private JButton submitButton;


	public void displayItem(Book book) {
		itemListArea.append(book.toString());
		itemListArea.append("\n");
	}
	
	public void displayNumberOfIn(int numIn) {
		copyInArea.append(String.valueOf(numIn));
		copyInArea.append("\n");
	}
	
	public void displayNumberOfOut(int numOut) {
		copyOutArea.append(String.valueOf(numOut));
		copyOutArea.append("\n");
	}
	
	public String getTitle() {
		return titleField.getText();
	}
	
	public String getAuthor() {
		return authorField.getText();
	}
	
	public String getSubject() {
		return subjectField.getText();
	}
		
	private void createTitleField() {

		titleField = new JTextField(255);		
		titleField.setName(TITLE_STRING);
		titleField.addKeyListener(new MyTextFieldKeyListener());		

		titleFieldLabel = new JLabel(TITLE_STRING + ":");
		titleFieldLabel.setLabelFor(titleField);        

	}
	
	private void createAuthorField() {

		authorField = new JTextField(255);
		authorField.setName(AUTHOR_STRING);
		authorField.addKeyListener(new MyTextFieldKeyListener());	
		
		authorFieldLabel = new JLabel(AUTHOR_STRING + ":");
		authorFieldLabel.setLabelFor(authorField);

	}
	
	private void createSubjectField() {

		subjectField = new JTextField(255);
		subjectField.setName(SUBJECT_STRING);
		subjectField.addKeyListener(new MyTextFieldKeyListener());	

		subjectFieldLabel = new JLabel(SUBJECT_STRING + ":");
		subjectFieldLabel.setLabelFor(subjectField);

	}
	
	private void createItemListPane() {

		itemListArea = new JTextArea();
		itemListArea.setEditable(false);

		itemListPane = new JScrollPane(itemListArea);
		itemListPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(ITEMLIST_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		itemListPane.setName(ITEMLIST_STRING);	

	}
	
	private void createCopyInPane() {

		copyInArea = new JTextArea();
		copyInArea.setEditable(false);

		copyInPane = new JScrollPane(copyInArea);
		copyInPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(COPYIN_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		copyInPane.setName(COPYIN_STRING);	

	}
	
	private void createCopyOutPane() {

		copyOutArea = new JTextArea();
		copyOutArea.setEditable(false);

		copyOutPane = new JScrollPane(copyOutArea);
		copyOutPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(COPYOUT_STRING),
				BorderFactory.createEmptyBorder(5,5,5,5)));	
		copyOutPane.setName(COPYOUT_STRING);	

	}
	
	private void insertToGrid(JLabel[] labels, JTextField[] textFields, 
			Container container) {

		GridBagConstraints gridc;		
		
		for (int i = 0; i < labels.length; i++) {
			gridc = new GridBagConstraints();
			gridc.anchor = GridBagConstraints.EAST;
			gridc.gridx = 0;
			gridc.gridy = i;
			gridc.weightx = 0;
			gridc.gridwidth = 1;
			container.add(labels[i], gridc);

			gridc = new GridBagConstraints();
			gridc.anchor = GridBagConstraints.WEST;
			gridc.gridx = 1;
			gridc.gridy = i;
			gridc.weightx = 1;
			gridc.gridwidth = GridBagConstraints.REMAINDER;
			gridc.fill = GridBagConstraints.HORIZONTAL;
			container.add(textFields[i], gridc);
		}
	}

	private void createSubmit() {
		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
	}

	public BorrowerUISearch(MainJFrame f) {	     

		setMainFrame(f);
		
		//Setting the border line around the panel
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Search Books"),
				BorderFactory.createEmptyBorder(10,10,10,10)));		

		//Grid Layout
		GridBagLayout gridb = new GridBagLayout();
		this.setLayout(gridb);

		//Create text fields for inputs
		createTitleField();
		createAuthorField();
		createSubjectField();
		createItemListPane();
		createCopyInPane();
		createCopyOutPane();
		createSubmit();
		
		//Group labels and text fields
		JTextField[] textFields = {titleField, authorField, subjectField};
		JLabel[] labels = {titleFieldLabel, authorFieldLabel, subjectFieldLabel};

		//Insert to the panel orderly
		insertToGrid(labels, textFields, this);

		//Insert spacing between the fields and the submit button
		GridBagConstraints gridc = new GridBagConstraints();
		gridc.gridy = textFields.length;
		this.add(Box.createVerticalStrut(10), gridc);
	
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		//Insert output panes
		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.weightx = 4;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(itemListPane, gridc);
		
		gridc = new GridBagConstraints();
		gridc.gridx = 1;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(copyInPane, gridc);
		
		gridc = new GridBagConstraints();
		gridc.gridx = 2;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.fill = GridBagConstraints.BOTH;
		panel.add(copyOutPane, gridc);

		gridc = new GridBagConstraints();
		gridc.gridx = 0;
		gridc.gridy = textFields.length;
		gridc.weightx = 1;
		gridc.weighty = 1;
		gridc.gridwidth = 2;
		gridc.gridheight = 5;
		gridc.fill = GridBagConstraints.BOTH;
		this.add(panel, gridc);
		
		//Insert the submit button
		gridc = new GridBagConstraints();
		gridc.anchor = GridBagConstraints.CENTER;
		gridc.weightx = 1;
		gridc.gridwidth = 2;
		gridc.gridy = GridBagConstraints.PAGE_END;
		gridc.fill = GridBagConstraints.HORIZONTAL;
		this.add(submitButton, gridc);
		
	}

	/**
	 * Invoked when the submit button is clicked.	 
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			// Re-run all listeners to make sure all format checking is valid.
			for (Component c: this.getComponents()) {
				for (FocusListener f: c.getFocusListeners()) {
					f.focusLost(new FocusEvent(c, FocusEvent.FOCUS_LOST));									
				}
			}

			if (titleField.getText().isEmpty() &&
					authorField.getText().isEmpty() &&
					subjectField.getText().isEmpty())
				throw new IllegalArgumentException("At least one keyword is required.");
			
			//Run the SQL method on a separate thread
			Thread thread = new Thread(new Runnable(){

				public void run() {
					
					JProgressBar progressBar = new JProgressBar();
					
					/**
					 *  try-finally so that it is guaranteed the submit button is 
					 *  re-enabled and progress bar is deleted at the end.
					 */
					try {
						submitButton.setEnabled(false);
						//Indeterminate progress bar
						addProgressBar(progressBar);
						
						itemListArea.setText("");
						copyInArea.setText("");
						copyOutArea.setText("");
						
						Book[] books = bApp().searchBooksByKeywords(getTitle(), getAuthor(), getSubject());
						displayOutput(books.length + "items found");
						
						for (int i=0; i<books.length; i++) {
							displayItem(books[i]);
							int numIn = bApp().getNumCopiesByStatus(books[i],CopyStatus.in);
							int numOut = bApp().getNumCopiesByStatus(books[i], CopyStatus.out);
							displayNumberOfIn(numIn);
							displayNumberOfOut(numOut);
						}

					} catch (Exception e) {
						displayErrorMessage(e.getMessage());
					} finally {
						submitButton.setEnabled(true);
						getThisPanel().remove(progressBar);
						getThisPanel().validate();
						getThisPanel().repaint();
					}

				}

			});

			thread.start();

		} catch (Exception ex) {
			throw new IllegalArgumentException("Please check the inputs again.");
		}
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
