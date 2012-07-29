package org.stratta.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import org.stratta.io.FileAccessor;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public final class DataModelProviders implements Iterable<DataModel> {

    private final ImmutableList<DataModel> _dataModels;
    private final ProviderCatalogs _providerCatalogs;
    private final FileAccessor _fileAccessor = new FileAccessor();

    public DataModelProviders() {
        ImmutableList.Builder<DataModel> builder = ImmutableList.builder();
        ServiceLoader<DataModel> loader = ServiceLoader.load(DataModel.class);
        ProviderCatalogs savedCatalogs = _fileAccessor.readCatalogs();

        for (DataModel model : loader) {
            builder.add(model);
        }

        _dataModels = builder.build();

        if (savedCatalogs != null) {
            savedCatalogs.updateProviders(_dataModels);
        }

        _providerCatalogs = new ProviderCatalogs(_dataModels);
    }

    public DataModel[] toArray() {
        return _dataModels.toArray(new DataModel[0]);
    }

    public void saveCatalogs() throws IOException {
        _fileAccessor.writeCatalogs(_providerCatalogs);
    }
    
    @Override
    public Iterator<DataModel> iterator() {
        return _dataModels.iterator();
    }

    public static class ProviderCatalogs implements Serializable {

        private final ImmutableMap<Class<? extends DataModel>, DataModel.CatalogMap> _catalogs;

        private ProviderCatalogs(List<DataModel> dataModels) {
            ImmutableMap.Builder<Class<? extends DataModel>, DataModel.CatalogMap> builder = ImmutableMap.builder();

            for (DataModel model : dataModels) {
                builder.put(model.getClass(), model.getCatalogMap());
            }

            _catalogs = builder.build();
        }

        private void updateProviders(List<DataModel> dataModels) {
            for (DataModel model : dataModels) {
                Class<? extends DataModel> modelClass = model.getClass();
                
                if (_catalogs.containsKey(modelClass)) {
                    model.setCatalogMap(_catalogs.get(modelClass));
                }
            }
        }
    }
}
