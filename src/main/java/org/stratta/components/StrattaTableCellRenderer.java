package org.stratta.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

public class StrattaTableCellRenderer extends JLabel implements
		TableCellRenderer {
	private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	private static final Border FOCUS_BORDER = new LineBorder(Color.BLACK, 1);

	private static final Color NULL_BACKGROUND = Color.LIGHT_GRAY;
	private static final Color NULL_SELECTION_BACKGROUND = new Color(133, 150,
			166);

	public StrattaTableCellRenderer() {
		super();
		setOpaque(true);
		setBorder(NO_FOCUS_BORDER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (table == null)
			return this;

		boolean isNull = (value == null);

		if (isSelected) {
			super.setForeground(table.getSelectionForeground());
			super.setBackground(isNull ? NULL_SELECTION_BACKGROUND : table
					.getSelectionBackground());
		} else {
			super.setForeground(table.getForeground());
			super.setBackground(isNull ? NULL_BACKGROUND : table
					.getBackground());
		}

		setFont(table.getFont());

		if (hasFocus)
			setBorder(FOCUS_BORDER);
		else
			setBorder(NO_FOCUS_BORDER);

		setValue(value);

		return this;
	}

	protected void setValue(Object value) {
		boolean isNull = (value == null);

		setText(isNull ? "null" : value.toString());
	}
}
