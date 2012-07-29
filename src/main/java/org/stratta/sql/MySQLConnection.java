package org.stratta.sql;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.stratta.exception.ExceptionHandler;



public class MySQLConnection {
	private static final Pattern _USE_PATTERN = Pattern.compile(
			"\\A\\s*use\\s+(\\S+?)(?:\\s|;)*\\Z", Pattern.CASE_INSENSITIVE);;

	private Connection _conn = null;
	private ConnectionInfo _connInfo = null;
	private boolean _connected = false;
	private String _schema = null;

	private final ExceptionHandler _exceptionHandler;

	public MySQLConnection(ExceptionHandler exceptionHandler) {
		Preconditions.checkNotNull(exceptionHandler);
		_exceptionHandler = exceptionHandler;
	}

	public boolean connect(ConnectionInfo connInfo) {
		Preconditions.checkNotNull(connInfo);

		if (_connected)
			disconnect();

		try {
			open(connInfo);

			_connInfo = connInfo;
			_connected = true;
		} catch (SQLException e) {
			_exceptionHandler.handle(e);
		}

		return _connected;
	}

	public void disconnect() {
		close();
		_connected = false;
		_connInfo = null;
		_schema = null;
	}

	public boolean isConnected() {
		if (_connected)
			if (isClosed()) {
				try {
					open();

					return true;
				} catch (SQLException e) {
					_exceptionHandler.handle(e);
				}
			} else
				return true;

		return false;
	}

	public ConnectionInfo getInfo() {
		return _connInfo;
	}

	public List<TableModel> execute(String statement) {
		Preconditions.checkNotNull(statement);

		if (!isConnected()) {
			_exceptionHandler.notConnectedToServer();
			return null;
		}
		
		List<TableModel> tableModels = new ArrayList<TableModel>();
		
		Matcher useMatcher = _USE_PATTERN.matcher(statement);
		if(useMatcher.matches()) {
			setSchema(useMatcher.group(1));
			return tableModels;
		}
		
		Statement stmt = null;

		try {
			stmt = _conn.createStatement();

			if (stmt.execute(statement))
				do {
					tableModels.add(new ResultsTableModel(stmt.getResultSet()));
				} while (stmt.getMoreResults());
		} catch (SQLException e) {
			_exceptionHandler.handle(e);
			tableModels = null;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// Ignore
				}
		}

		return tableModels;
	}

	public void dispose() {
		disconnect();
		_conn = null;
	}

	public void setSchema(String schema) {
		Preconditions.checkNotNull(schema);

		if (isConnected())
			try {
				_conn.setCatalog(schema);
				_schema = schema;
			} catch (SQLException e) {
				_exceptionHandler.handle(e);
			}
		else
			_exceptionHandler.notConnectedToServer();
	}

	public String getSchema() {
		return _schema;
	}

	private void open(ConnectionInfo connInfo) throws SQLException {
		_conn = DriverManager.getConnection(connInfo.getConnectionString());

		if (_schema != null)
			setSchema(_schema);
	}

	private void open() throws SQLException {
		if (_connected)
			open(_connInfo);
		else
			throw new SQLException();
	}

	private void close() {
		if (!isClosed())
			try {
				_conn.close();
			} catch (SQLException e) {
				_exceptionHandler.handle(e);
			}
	}

	private boolean isClosed() {
		try {
			return (_conn == null || _conn.isClosed());
		} catch (SQLException e) {
			_exceptionHandler.handle(e);
			return true;
		}
	}
}
