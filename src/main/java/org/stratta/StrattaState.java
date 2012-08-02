package org.stratta;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import org.stratta.io.FileAccessor;
import org.stratta.sql.ConnectionInfo;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public final class StrattaState implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String _FILE_NAME = "state.dat";
    private final Set<ConnectionInfo> _connHistory = new TreeSet<>();
    private ConnectionInfo _mostRecent = null;

    private StrattaState() {
    }

    public void save() throws IOException {
        FileAccessor accessor = new FileAccessor();

        accessor.writeObject(_FILE_NAME, this);
    }

    public static StrattaState load() {
        FileAccessor accessor = new FileAccessor();
        
        StrattaState state = accessor.readObject(_FILE_NAME, StrattaState.class);
        
        return (state != null) ? state : new StrattaState();
    }

    public ConnectionInfo getMostRecentConn() {
        return _mostRecent;
    }

    public ConnectionInfo[] getConnHistory() {
        return _connHistory.toArray(new ConnectionInfo[0]);
    }

    public void connMade(ConnectionInfo connInfo) {
        Preconditions.checkNotNull(connInfo);

        _connHistory.remove(connInfo);
        _connHistory.add(connInfo);

        _mostRecent = connInfo;
    }
}
