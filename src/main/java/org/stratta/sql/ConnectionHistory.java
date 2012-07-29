package org.stratta.sql;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * The history of successful database connections.
 * 
 * @author Joshua Swank
 */
public class ConnectionHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Set<ConnectionInfo> _connections = new TreeSet<>();;
	
	private ConnectionInfo _mostRecent;
	
	public ConnectionHistory() {
	}
	
	public void connectionMade(ConnectionInfo connInfo) {
		Preconditions.checkNotNull(connInfo);
		
		_connections.remove(connInfo);
		_connections.add(connInfo);
		
		_mostRecent = connInfo;
	}
	
	public ConnectionInfo getMostRecent() {
		return _mostRecent;
	}
	
	public ConnectionInfo[] getConnections() {
		return _connections.toArray(new ConnectionInfo[0]);
	}
	
	@Override
	public String toString() {
		return _connections.toString();
	}
}
