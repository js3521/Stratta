package org.stratta.sql;

import org.stratta.security.Password;
import java.io.Serializable;

/**
 * Holds the host and authentication information of a database server. It is
 * immutable.
 * 
 * @author Joshua Swank
 */
public final class ConnectionInfo implements Comparable<ConnectionInfo>,
		Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * The default hostname.
	 */
	public static final String DEFAULT_HOST = "localhost";

	/**
	 * The default user.
	 */
	public static final String DEFAULT_USER = "root";

	/**
	 * The default port.
	 */
	public static final int DEFAULT_PORT = 3306;

	private final String _host;
	private final int _port;
	private final String _user;
	private final Password _password;
	private final String _connectionString;

	/**
	 * Constructs a <tt>ConnectionInfo</tt> object.
	 * 
	 * @param host
	 *            the server's hostname
	 * @param port
	 *            the port used to connect to the server
	 * @param user
	 *            the user connections are established as
	 * @param password
	 *            the user's password
	 */
	public ConnectionInfo(String host, int port, String user, Password password) {
		_host = (host == null || host.isEmpty()) ? DEFAULT_HOST : host;
		_port = (port < 0 || port > 65535) ? DEFAULT_PORT : port;
		_user = (user == null || user.isEmpty()) ? DEFAULT_USER : user;
		_password = (password == null) ? new Password() : password;
		_connectionString = buildConnectionString();
	}

	/**
	 * Constructs a <tt>ConnectionInfo</tt> object with no password.
	 * 
	 * @param host
	 *            the server's hostname
	 * @param port
	 *            the port used to connect to the server
	 * @param user
	 *            the user connections are established as
	 */
	public ConnectionInfo(String host, int port, String user) {
		this(host, port, user, new Password());
	}

	/**
	 * Constructs a <tt>ConnectionInfo</tt> object with the default properties.
	 */
	public ConnectionInfo() {
		this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_USER, new Password());
	}

	/**
	 * Returns the hostname of the target server.
	 * 
	 * @return the hostname of the target server
	 */
	public String getHost() {
		return _host;
	}

	public int getPort() {
		return _port;
	}

	public String getHostWithPort() {
		return String.format("%s:%d", _host, _port);
	}
	
	public String getUser() {
		return _user;
	}

	@Override
	public int compareTo(ConnectionInfo o) {
		return _host.compareTo(o._host);
	}

	@Override
	public String toString() {
		return _host;
	}

	public Password getPassword() {
		return _password;
	}

	public String getConnectionString() {
		return _connectionString;
	}
	
	private String buildConnectionString() {
		StringBuilder builder = new StringBuilder();

		builder.append(String.format("jdbc:mysql://%s:%d?user=%s", _host,
				_port, _user));

		if (!_password.isBlank())
			builder.append(String.format("&password=%s",
					new String(_password.getPassword())));

		return builder.toString();
	}
}
