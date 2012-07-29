package org.stratta.sql;

import com.google.common.base.Preconditions;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ResultsTableModel extends DefaultTableModel {

    public ResultsTableModel(ResultSet data) throws SQLException {
        Preconditions.checkNotNull(data);

        ResultSetMetaData meta = data.getMetaData();
        int nColumns = meta.getColumnCount();

        for (int i = 0; i < nColumns; i++) {
            addColumn(meta.getColumnName(i + 1));
        }

        while (data.next()) {
            Object[] row = new Object[nColumns];

            for (int i = 0; i < nColumns; i++) {
                row[i] = data.getString(i + 1);
            }

            addRow(row);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
