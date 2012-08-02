package org.stratta;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.stratta.io.FileAccessor;
import org.stratta.model.DataModel;
import org.stratta.sql.ConnectionInfo;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public final class StrattaState implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String _FILE_NAME = "state.dat";
    private final Set<ConnectionInfo> _connHistory = new TreeSet<>();
    private ConnectionInfo _mostRecent;
    private ImmutableMap<Class<? extends DataModel>, DataModel.CatalogMap> _providerCatalogs;

    private StrattaState() {
        _mostRecent = null;
        _providerCatalogs = ImmutableMap.of();
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
    
    public void setProviderCatalogs(List<DataModel> providers) {
        Preconditions.checkNotNull(providers);
        ImmutableMap.Builder<Class<? extends DataModel>, DataModel.CatalogMap> builder =
                ImmutableMap.builder();
        
        for(DataModel provider : providers) {
            builder.put(provider.getClass(), provider.getCatalogMap());
        }
        
        _providerCatalogs = builder.build();
    }
    
    public ImmutableMap<Class<? extends DataModel>, DataModel.CatalogMap> getProviderCatalogs() {
        return _providerCatalogs;
    }
}
