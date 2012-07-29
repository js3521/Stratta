package org.stratta.sql;

import org.stratta.security.Password;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.stratta.StrattaVersion;
import org.stratta.Stratta;
import org.stratta.sql.ConnectionHistory;
import org.stratta.sql.ConnectionInfo;
import org.stratta.components.JComboTextBox;
import org.stratta.components.StrattaInsets;

import com.google.common.base.Preconditions;

/**
 * A dialog for editing database connection information.
 * 
 * @author Joshua Swank
 */
public class ConnectionDialog extends JDialog implements ActionListener,
		WindowListener, ItemListener {

	// Controls
	private final JComboTextBox<ConnectionInfo> _cboHost = new JComboTextBox<>();
	private final JTextField _txtUser = new JTextField(16),
			_txtPort = new JTextField(5);
	private final JPasswordField _txtPassword = new JPasswordField(16);
	private final JCheckBox _chkStorePassword = new JCheckBox("Store Password");
	private final JButton _btnConnect = new JButton("Connect"),
			_btnClear = new JButton("Clear"),
			_btnCancel = new JButton("Cancel");

	private final Stratta _application;
	private final ConnectionHistory _history;

	public ConnectionDialog(JFrame owner, Stratta application,
			ConnectionHistory history) {
		super(owner, StrattaVersion.TITLE, true);

		// Check preconditions and initialize fields
		Preconditions.checkNotNull(application);
		_application = application;

		Preconditions.checkNotNull(history);
		_history = history;

		// Set component states
		populateHistoryData();

		// Set dialog state
		setResizable(false);
		addWindowListener(this);
		getRootPane().setDefaultButton(_btnConnect);

		// Add component listeners
		_btnConnect.addActionListener(this);
		_btnClear.addActionListener(this);
		_btnCancel.addActionListener(this);

		_cboHost.addItemListener(this);

		addComponents();
		
		pack();
		setLocationRelativeTo(owner);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == _btnConnect)
			_btnConnect_ActionPerformed(e);
		else if (source == _btnClear)
			_btnClear_ActionPerformed(e);
		else if (source == _btnCancel)
			_btnCancel_ActionPerformed(e);
	}

	public void windowClosing(WindowEvent e) {
		_application.closeConnectionDialog();
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED
				&& e.getItem() instanceof ConnectionInfo)
			updateInfo((ConnectionInfo) e.getItem());
	}

	private void populateHistoryData() {
		DefaultComboBoxModel<ConnectionInfo> model = new DefaultComboBoxModel<>(
				_history.getConnections());
		_cboHost.setModel(model);

		ConnectionInfo mostRecent = _history.getMostRecent();

		_cboHost.setSelectedItem(mostRecent);
		updateInfo(mostRecent);
	}

	private void updateInfo(ConnectionInfo connInfo) {
		if (connInfo != null) {
			Password password = connInfo.getPassword();

			_txtPort.setText(String.valueOf(connInfo.getPort()));
			_txtUser.setText(connInfo.getUser());

			if (!password.isBlank()) {
				_txtPassword.setText(new String(password.getPassword()));
				_chkStorePassword.setSelected(true);
			} else {
				_txtPassword.setText(new String());
				_chkStorePassword.setSelected(false);
			}
		}
	}

	private void _btnConnect_ActionPerformed(ActionEvent e) {
		_application.connect(getConnectionInfo());
	}

	private void _btnClear_ActionPerformed(ActionEvent e) {
		_cboHost.setSelectedIndex(-1);
		_txtPort.setText(new String());
		_txtUser.setText(new String());
		_txtPassword.setText(new String());
	}

	private void _btnCancel_ActionPerformed(ActionEvent e) {
		_application.closeConnectionDialog();
	}

	private ConnectionInfo getConnectionInfo() {
		Password password = new Password(_txtPassword.getPassword(),
				_chkStorePassword.isSelected());

		return new ConnectionInfo(_cboHost.getText(), getPort(),
				_txtUser.getText(), password);
	}

	private int getPort() {
		try {
			return Integer.valueOf(_txtPort.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void addComponents() {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = StrattaInsets.DEFAULT;
		add(buildConnectionPanel(), c);

		c.gridy = 1;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.insets = StrattaInsets.BOTTOM_FAR;
		add(buildButtonsPanel(), c);
	}

	private JPanel buildConnectionPanel() {
		JPanel pnlConnection = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		pnlConnection.setBorder(new TitledBorder("Connect to MySQL Server"));

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.EAST;
		pnlConnection.add(new Label("Host"), c);

		c.gridy = 1;
		pnlConnection.add(new Label("Port"), c);

		c.gridy = 2;
		pnlConnection.add(new Label("User"), c);

		c.gridy = 3;
		pnlConnection.add(new Label("Password"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		pnlConnection.add(_cboHost, c);

		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		pnlConnection.add(_txtPort, c);

		c.gridy = 2;
		pnlConnection.add(_txtUser, c);

		c.gridy = 3;
		pnlConnection.add(_txtPassword, c);

		c.gridy = 4;
		pnlConnection.add(_chkStorePassword, c);

		return pnlConnection;
	}

	private JPanel buildButtonsPanel() {
		JPanel pnlButtons = new JPanel(new GridLayout(1, 3,
				StrattaInsets.DEFAULT_GAP, 0));

		pnlButtons.add(_btnConnect);
		pnlButtons.add(_btnClear);
		pnlButtons.add(_btnCancel);

		return pnlButtons;
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
