package org.stratta;

import java.io.IOException;
import org.stratta.exception.ExceptionHandler;
import org.stratta.model.DataModelProviders;
import org.stratta.sql.MySQLConnection;
import org.stratta.sql.ConnectionInfo;
import org.stratta.sql.ConnectionDialog;

public class Stratta {

    private final ExceptionHandler _exceptionHandler = new ExceptionHandler();
    private final MySQLConnection _conn = new MySQLConnection(_exceptionHandler);
    private final StrattaState _state = StrattaState.load();
    private final DataModelProviders _dataModels = new DataModelProviders(_state);
    private ConnectionDialog _connectionDialog;
    private StrattaFrame _strattaFrame;
    private SettingsDialog _settingsDialog;

    public Stratta() {
        showStrattaFrame();
    }

    public void closeConnectionDialog() {
        disposeConnectionDialog();
    }

    public void closeSettingsDialog() {
        disposeSettingsDialog();
    }

    public void closeStrattaFrame() {
        dispose();
    }

    public void connect(ConnectionInfo connInfo) {
        if (_conn.connect(connInfo)) {
            _state.connMade(connInfo);

            try {
                _state.save();
            } catch (IOException e) {
                _exceptionHandler.handle(e);
            }

            disposeConnectionDialog();
        }

        _strattaFrame.connectionUpdated();
    }

    public void disconnect() {
        _conn.disconnect();
        _strattaFrame.connectionUpdated();
    }

    public void showConnectionDialog() {
        _connectionDialog = new ConnectionDialog(_strattaFrame, this, _state);

        _connectionDialog.setVisible(true);
    }

    public void showSettingsDialog(int tab) {
        _settingsDialog = new SettingsDialog(_strattaFrame, this, _exceptionHandler, _dataModels, tab);

        _settingsDialog.setVisible(true);
    }

    private void dispose() {
        disposeConnectionDialog();
        disposeStrattaFrame();
        _conn.dispose();
    }

    private void showStrattaFrame() {
        _strattaFrame = new StrattaFrame(this, _conn);
        _exceptionHandler.setOwner(_strattaFrame);

        _strattaFrame.setVisible(true);
    }

    private void disposeConnectionDialog() {
        if (_connectionDialog != null) {
            _connectionDialog.dispose();
            _connectionDialog = null;
        }
    }

    private void disposeSettingsDialog() {
        if (_settingsDialog != null) {
            _settingsDialog.dispose();
            _settingsDialog = null;
        }
    }

    private void disposeStrattaFrame() {
        if (_strattaFrame != null) {
            _strattaFrame.dispose();
            _strattaFrame = null;

            _exceptionHandler.setOwner(null);
        }
    }

    public static void main(String[] args) {
        new Stratta();
    }
}
