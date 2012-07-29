package org.stratta.io;

import com.google.common.base.Preconditions;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.stratta.exception.ExceptionHandler;
import org.stratta.sql.ConnectionHistory;

/**
 * The Stratta file access object.
 *
 * @author Joshua Swank
 */
public final class FileAccessor {

    private static final String _HISTORY_FILE = "history.dat";
    private final ExceptionHandler _exceptionHandler;

    public FileAccessor(ExceptionHandler exceptionHandler) {
        Preconditions.checkNotNull(exceptionHandler);

        _exceptionHandler = exceptionHandler;
    }

    public ConnectionHistory readHistory() {
        ConnectionHistory history = readObject(_HISTORY_FILE,
                ConnectionHistory.class);

        if (history == null) {
            history = new ConnectionHistory();
        }

        return history;
    }

    public void writeHistory(ConnectionHistory history) {
        Preconditions.checkNotNull(history);

        try {
            writeObject(_HISTORY_FILE, history);
        } catch (IOException e) {
            _exceptionHandler.handle(e);
        }
    }

    private <T> T readObject(String file, Class<T> c) {
        Object obj = null;

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                    file));

            try {
                obj = in.readObject();
            } catch (ClassNotFoundException e) {
                // Ignore
            }

            in.close();
        } catch (IOException e) {
            // Ignore
        }

        return (T) obj;
    }

    private void writeObject(String file, Object obj) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                file));

        out.writeObject(obj);

        out.close();
    }
}
