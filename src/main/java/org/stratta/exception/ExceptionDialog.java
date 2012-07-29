package org.stratta.exception;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.stratta.StrattaVersion;
import org.stratta.components.StrattaInsets;
import org.stratta.components.StrattaInsets;
import org.stratta.StrattaVersion;

/**
 * A dialog for displaying runtime exceptions.
 * 
 * @author Joshua Swank
 */
public class ExceptionDialog extends JDialog implements ActionListener,
		WindowListener {
	private final JButton _btnClose = new JButton("Close");

	public ExceptionDialog(JFrame owner, String message, String heading) {
		super(owner, StrattaVersion.TITLE, true);

		// Check preconditions and initialize fields
		message = (message != null) ? message : new String();
		heading = (heading != null) ? heading : new String();

		// Set dialog state
		setResizable(false);
		setSize(480, 320);
		addWindowListener(this);

		// Add component listeners
		_btnClose.addActionListener(this);

		addComponents(message, heading);
		
		setLocationRelativeTo(owner);
	}

	public ExceptionDialog(JFrame owner, Exception exception) {
		this(owner, exception.toString(), "An exception has occurred:");
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == _btnClose)
			_btnClose_ActionPerformed(e);
	}
	
	private void _btnClose_ActionPerformed(ActionEvent e) {
		dispose();
	}

	public void windowClosing(WindowEvent e) {
		dispose();
	}

	private void addComponents(String message, String heading) {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = StrattaInsets.TOP_NEAR;
		add(new JLabel(heading), c);

		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = StrattaInsets.DEFAULT;
		add(buildTextArea(message), c);

		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.insets = StrattaInsets.BOTTOM_FAR;
		add(_btnClose, c);
	}

	private JScrollPane buildTextArea(String message) {
		JTextArea taMessage = new JTextArea();

		taMessage.setEditable(false);
		taMessage.setLineWrap(true);
		taMessage.setWrapStyleWord(true);
		taMessage.setText(message);
		taMessage.setCaretPosition(0);

		return new JScrollPane(taMessage,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}