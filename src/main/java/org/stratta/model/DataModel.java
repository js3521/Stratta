package org.stratta.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Set;

public abstract class DataModel {

    private final CatalogMap _catalogs;

    public DataModel() {
        _catalogs = new CatalogMap(getCatalogNames());
    }

    public final ImmutableMap<String, String> getCatalogs() {
        return ImmutableMap.copyOf(_catalogs);
    }

    @Override
    public final String toString() {
        return getName();
    }

    public final void setCatalog(String name, String catalog) {
        _catalogs.setCatalog(name, catalog);
    }
    
    public final CatalogMap getCatalogMap() {
        return _catalogs;
    }
    
    protected final void setCatalogMap(CatalogMap catalogMap) {
        _catalogs.setCatalogs(catalogMap);
    }

    public abstract String getName();

    public abstract Set<String> getCatalogNames();

    public static final class CatalogMap extends HashMap<String, String> {

        private CatalogMap(Set<String> catalogNames) {
            Preconditions.checkNotNull(catalogNames);

            for (String name : catalogNames) {
                super.put(name, new String());
            }
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String put(String name, String catalog) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String remove(Object key) {
            throw new UnsupportedOperationException();
        }

        private String setCatalog(String name, String catalog) {
            Preconditions.checkArgument(this.containsKey(name));
            Preconditions.checkNotNull(catalog);

            return super.put(name, catalog);
        }
        
        private void setCatalogs(CatalogMap catalogMap) {
            Preconditions.checkNotNull(catalogMap);
            
            for(String name : catalogMap.keySet())
                setCatalog(name, catalogMap.get(name));
        }
    }
}
