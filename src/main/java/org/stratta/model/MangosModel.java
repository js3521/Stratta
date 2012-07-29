package org.stratta.model;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public class MangosModel extends DataModel {

    private final String _NAME = "Mangos";

    public MangosModel() {
        super();
    }

    @Override
    public String getName() {
        return _NAME;
    }

    @Override
    public Set<String> getCatalogNames() {
        Set<String> catalogNames = new HashSet<>();
        
        catalogNames.add("characters");
        catalogNames.add("realmd");
        catalogNames.add("world");
        
        return catalogNames;
    }
}
