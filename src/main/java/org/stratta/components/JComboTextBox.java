package org.stratta.components;

import javax.swing.JComboBox;

public class JComboTextBox<T> extends JComboBox<T> {
	public JComboTextBox() {
		setEditable(true);
	}
	
	public String getText() {
		Object selectedItem = getSelectedItem();
		
		return (selectedItem != null) ? selectedItem.toString() : new String();
	}
}
