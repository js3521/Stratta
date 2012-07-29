package org.stratta.sql;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import org.stratta.sql.MySQLConnection;
import org.stratta.components.StrattaInsets;
import org.stratta.components.StrattaTableCellRenderer;

import com.google.common.base.Preconditions;

public class SQLPanel extends JPanel implements ActionListener {
	private final JTextArea _taStatement = new JTextArea();
	private final JButton _btnExecute = new JButton("Execute");
	private final JTabbedPane _tpResults = new JTabbedPane();

	private final MySQLConnection _conn;

	public SQLPanel(MySQLConnection conn) {
		// Check preconditions and initialize fields
		Preconditions.checkNotNull(conn);
		_conn = conn;

		// Set component states
		_taStatement.setLineWrap(true);
		_taStatement.setWrapStyleWord(true);
		_taStatement.setRows(6);

		// Set panel state

		// Add component listeners
		_btnExecute.addActionListener(this);

		addComponents();
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == _btnExecute)
			_btnExecute_ActionPerformed(e);
	}

	private void _btnExecute_ActionPerformed(ActionEvent e) {
		List<TableModel> models = _conn.execute(_taStatement.getText());
		
		if (models != null && models.size() > 0) {
			_tpResults.removeAll();

			for (int i = 0; i < models.size(); i++) {
				JScrollPane spResultSet = buildResultsTable(models.get(i));

				_tpResults.addTab(String.format("ResultSet%d", i + 1),
						spResultSet);
			}
		}
	}
	
	private JScrollPane buildResultsTable(TableModel model) {
		JTable tblResults = new JTable(model);
		JScrollPane spResults = new JScrollPane(tblResults,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tblResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblResults.setDefaultRenderer(Object.class,
				new StrattaTableCellRenderer());
		
		return spResults;
	}

	private void addComponents() {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		JScrollPane spStatement = new JScrollPane(_taStatement,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		spStatement.setMinimumSize(_taStatement.getPreferredSize());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = StrattaInsets.DEFAULT;
		add(spStatement, c);

		c.gridx = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new StrattaInsets(true, false, true, true);
		add(_btnExecute, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		add(_tpResults, c);

	}
}
