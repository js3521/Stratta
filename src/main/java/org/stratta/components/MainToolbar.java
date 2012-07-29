package org.stratta.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.stratta.sql.MySQLConnection;

import com.google.common.base.Preconditions;
import org.stratta.Stratta;

public class MainToolbar extends JToolBar implements ActionListener {
	private final JButton _btnConnect = new JButton(StrattaIcon.CONNECT),
			_btnDisconnect = new JButton(StrattaIcon.DISCONNECT);

	private final Stratta _application;
	private final MySQLConnection _conn;

	public MainToolbar(Stratta application, MySQLConnection conn) {
		// Check preconditions and initialize fields
		Preconditions.checkNotNull(application);
		_application = application;
		
		Preconditions.checkNotNull(conn);
		_conn = conn;
		
		// Set component states
		_btnConnect.setToolTipText("Connect...");
		_btnDisconnect.setToolTipText("Disconnect");

		// Set toolbar state
		setFloatable(false);
		setBorderPainted(true);

		// Add component listeners
		_btnConnect.addActionListener(this);
		_btnDisconnect.addActionListener(this);

		addComponents();
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == _btnConnect)
			_btnConnect_ActionPerformed(e);
		else if(source == _btnDisconnect)
			_btnDisconnect_ActionPerformed(e);
	}
	
	public void connectionUpdated() {
		reinitialize();
	}

	private void _btnConnect_ActionPerformed(ActionEvent e) {
		_application.showConnectionDialog();
	}
	
	private void _btnDisconnect_ActionPerformed(ActionEvent e) {
		_application.disconnect();
	}
	
	private void reinitialize() {
		removeAll();
		addComponents();
	}

	private void addComponents() {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		add((_conn.isConnected()) ? _btnDisconnect :_btnConnect, c);
	}
}
