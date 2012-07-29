package org.stratta.exception;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.stratta.StrattaVersion;
import org.stratta.StrattaVersion;

public class ExceptionHandler {
	private JFrame _owner;

	public ExceptionHandler(JFrame owner) {
		_owner = owner;
	}

	public ExceptionHandler() {
		this(null);
	}

	public boolean hasOwner() {
		return (_owner != null);
	}

	public void setOwner(JFrame owner) {
		_owner = owner;
	}

	public void handle(Exception e) {
		ExceptionDialog exceptionDialog = new ExceptionDialog(_owner, e);
		exceptionDialog.setVisible(true);
	}
	
	public void handleRuntime(RuntimeException e) {
		handle(e);
	}

	public void notConnectedToServer() {
		simpleError("You are not connected to a database.");
	}
	
	private void simpleError(String message) {
		JOptionPane.showMessageDialog(null, message, StrattaVersion.TITLE,
				JOptionPane.ERROR_MESSAGE);
	}
}
