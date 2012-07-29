package org.stratta;

import com.google.common.base.Preconditions;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.stratta.exception.ExceptionHandler;
import org.stratta.model.DataModelProviders;
import org.stratta.model.ModelSelectionPanel;

public class SettingsDialog extends JDialog implements ActionListener, WindowListener {

    public static final int DATAMODEL_TAB = 0;
    private final DataModelProviders _providers;
    private final ModelSelectionPanel _pnlDataModel;
    private final Stratta _application;
    private final ExceptionHandler _exceptionHandler;
    private final JButton _btnOK = new JButton("OK"),
            _btnCancel = new JButton("Cancel");

    public SettingsDialog(JFrame owner, Stratta application, ExceptionHandler exceptionHandler, DataModelProviders providers, int tab) {
        super(owner, "Settings", true);

        // Check preconditions and initialize fields
        Preconditions.checkNotNull(application);
        _application = application;
        
        Preconditions.checkNotNull(exceptionHandler);
        _exceptionHandler = exceptionHandler;

        Preconditions.checkNotNull(providers);
        _providers = providers;

        _pnlDataModel = new ModelSelectionPanel(_providers);

        // Set component states

        // Set dialog state
        setResizable(false);
        addWindowListener(this);
        getRootPane().setDefaultButton(_btnOK);
        setSize(360, 480);

        // Add component listeners
        _btnOK.addActionListener(this);
        _btnCancel.addActionListener(this);

        addComponents(tab);

        setLocationRelativeTo(owner);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == _btnOK) {
            _btnOK_ActionPerformed(e);
        } else if (source == _btnCancel) {
            _btnCancel_ActionPerformed(e);
        }
    }

    private void _btnOK_ActionPerformed(ActionEvent e) {
        try {
            _pnlDataModel.saveCatalogs();
        } catch (IOException ex) {
           _exceptionHandler.handle(ex);
        }
        _application.closeSettingsDialog();
    }

    private void _btnCancel_ActionPerformed(ActionEvent e) {
        _application.closeSettingsDialog();
    }

    public void windowClosing(WindowEvent e) {
        _application.closeSettingsDialog();
    }

    private void addComponents(int tab) {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        add(buildSettingsPanel(tab), c);

        c.gridy = 1;
        c.weighty = 0;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = Spacing.getBottomInsets(true);
        add(buildButtonsPanel(), c);
    }

    private JTabbedPane buildSettingsPanel(int tab) {
        JTabbedPane tpSettings = new JTabbedPane();

        tpSettings.addTab("Data Model", _pnlDataModel);

        tpSettings.setSelectedIndex(tab);

        return tpSettings;
    }

    private JPanel buildButtonsPanel() {
        JPanel pnlButtons = new JPanel(Spacing.getHorizontalLayout(2));

        pnlButtons.add(_btnOK);
        pnlButtons.add(_btnCancel);

        return pnlButtons;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
