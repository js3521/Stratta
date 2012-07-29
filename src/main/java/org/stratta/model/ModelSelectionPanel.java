package org.stratta.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.stratta.Spacing;

public class ModelSelectionPanel extends JPanel implements ItemListener {

    private final JComboBox _cboDataModel;
    private final JPanel _pnlCatalogs = new JPanel(new GridBagLayout());
    private final Map<String, JTextField> _catalogFields = new HashMap<>();
    private final DataModelProviders _providers;

    public ModelSelectionPanel(DataModelProviders providers, DataModel currentModel) {
        // Check preconditions and initialize fields
        Preconditions.checkNotNull(providers);
        _providers = providers;

        // Set component states
        _cboDataModel = new JComboBox(new DefaultComboBoxModel(_providers.toArray()));
        _cboDataModel.setSelectedIndex(-1);

        _pnlCatalogs.setBorder(new TitledBorder("Catalogs"));
        updateCatalogssPanel(currentModel);

        // Set panel state

        // Add component listeners
        _cboDataModel.addItemListener(this);

        addComponents();
    }

    public ModelSelectionPanel(DataModelProviders providers) {
        this(providers, null);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        
        if(source == _cboDataModel)
        {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                saveCurrentCatalogs((DataModel) e.getItem());
            }

            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateCatalogssPanel((DataModel) e.getItem());
            }
        }
    }
    
    public void saveCatalogs() throws IOException {
        DataModel selectedModel = (DataModel)_cboDataModel.getSelectedItem();
        
        if(selectedModel != null)
            saveCurrentCatalogs(selectedModel);
        
        _providers.saveCatalogs();
    }

    private void saveCurrentCatalogs(DataModel currentModel) {
        Set<String> catalogNames = _catalogFields.keySet();

        for (String name : catalogNames) {
            currentModel.setCatalog(name, _catalogFields.get(name).getText());
        }
    }

    private void addComponents() {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = Spacing.getDefaultInsets();
        add(buildSelectionPanel(), c);

        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        add(_pnlCatalogs, c);
    }

    private JPanel buildSelectionPanel() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel pnlSelection = new JPanel(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = Spacing.getDefaultInsets();
        pnlSelection.add(new JLabel("Data Model"), c);

        c.gridx = 1;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnlSelection.add(_cboDataModel, c);

        return pnlSelection;
    }

    private void updateCatalogssPanel(DataModel model) {
        _pnlCatalogs.removeAll();
        _catalogFields.clear();

        if (model != null) {
            ImmutableMap<String, String> catalogs = model.getCatalogs();
            String[] catalogNames = catalogs.keySet().toArray(new String[0]);

            for (String name : catalogNames) {
                JTextField txtCatalog = new JTextField(catalogs.get(name));
                _catalogFields.put(name, txtCatalog);
            }

            GridBagConstraints c = new GridBagConstraints();

            c.gridy = 1;
            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.NORTH;
            c.fill = GridBagConstraints.HORIZONTAL;

            _pnlCatalogs.add(buildCatalogFieldsPanel(catalogNames), c);
        }

        _pnlCatalogs.revalidate();
        _pnlCatalogs.repaint();
    }

    private JPanel buildCatalogFieldsPanel(String[] catalogNames) {
        JPanel pnlCatalogFields = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = Spacing.getDefaultInsets();

        for (int i = 0; i < catalogNames.length; i++) {
            String name = catalogNames[i];

            c.gridx = 0;
            c.gridy = i;
            c.weightx = 0;
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            pnlCatalogFields.add(new JLabel(name), c);

            c.gridx = 1;
            c.weightx = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            pnlCatalogFields.add(_catalogFields.get(name), c);
        }

        return pnlCatalogFields;
    }
}
