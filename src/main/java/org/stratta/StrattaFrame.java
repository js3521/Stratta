package org.stratta;

import org.stratta.sql.SQLPanel;
import com.google.common.base.Preconditions;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.stratta.StrattaVersion;
import org.stratta.sql.MySQLConnection;
import org.stratta.Stratta;
import org.stratta.Stratta;
import org.stratta.StrattaVersion;
import org.stratta.components.MainMenuBar;
import org.stratta.components.MainToolbar;

public class StrattaFrame extends JFrame implements WindowListener {

    private final JTabbedPane _tpMain = new JTabbedPane();
    private final MainToolbar _tlbMain;
    private final Stratta _application;
    private final MySQLConnection _conn;

    public StrattaFrame(Stratta application, MySQLConnection conn) {
        // Check preconditions and initialize fields
        Preconditions.checkNotNull(application);
        _application = application;

        Preconditions.checkNotNull(conn);
        _conn = conn;

        _tlbMain = new MainToolbar(_application, _conn);

        // Set component states
        _tpMain.addTab("SQL", new SQLPanel(_conn));

        // Set frame state
        setSize(800, 600);
        setJMenuBar(new MainMenuBar(_application));
        updateTitle();
        addWindowListener(this);

        addComponents();

        setLocationRelativeTo(null);
    }

    public void windowClosing(WindowEvent e) {
        _application.closeStrattaFrame();
    }

    public void connectionUpdated() {
        updateTitle();
        _tlbMain.connectionUpdated();
    }

    private void addComponents() {
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(_tlbMain, c);

        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        add(_tpMain, c);
    }

    private void updateTitle() {
        StringBuilder builder = new StringBuilder(StrattaVersion.TITLE);

        builder.append(" - ");

        if (_conn.isConnected()) {
            builder.append(_conn.getInfo().getHostWithPort());
        } else {
            builder.append("Not Connected");
        }

        builder.append(" / ");

        builder.append("No Data Model");

        setTitle(builder.toString());
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