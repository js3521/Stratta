package org.stratta.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import org.stratta.StrattaState;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public final class DataModelProviders implements Iterable<DataModel> {

    private final ImmutableList<DataModel> _dataModels;
    private final StrattaState _state;

    public DataModelProviders(StrattaState state) {
        Preconditions.checkNotNull(state);
        _state = state;

        ImmutableList.Builder<DataModel> builder = ImmutableList.builder();
        ServiceLoader<DataModel> loader = ServiceLoader.load(DataModel.class);

        for (DataModel model : loader) {
            builder.add(model);
        }

        _dataModels = builder.build();

        updateProviders(_state.getProviderCatalogs(), _dataModels);
        _state.setProviderCatalogs(_dataModels);
    }

    public DataModel[] toArray() {
        return _dataModels.toArray(new DataModel[0]);
    }

    public void saveCatalogs() throws IOException {
        _state.save();
    }

    @Override
    public Iterator<DataModel> iterator() {
        return _dataModels.iterator();
    }

    private void updateProviders(Map<Class<? extends DataModel>, DataModel.CatalogMap> providerCatalogs,
            List<DataModel> dataModels) {
        for (DataModel model : dataModels) {
            Class<? extends DataModel> modelClass = model.getClass();

            if (providerCatalogs.containsKey(modelClass)) {
                model.setCatalogMap(providerCatalogs.get(modelClass));
            }
        }
    }
}
