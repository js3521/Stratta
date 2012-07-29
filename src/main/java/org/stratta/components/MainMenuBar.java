package org.stratta.components;

import org.stratta.SettingsDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.stratta.Stratta;

import com.google.common.base.Preconditions;
import org.stratta.Stratta;

public class MainMenuBar extends JMenuBar implements ActionListener {
	private final JMenuItem _mniFileExit = new JMenuItem("Exit"),
			_mniSettingsDataModel = new JMenuItem("Data Model");
	
	private final Stratta _application;

	public MainMenuBar(Stratta application) {
		// Check preconditions and initialize fields
		Preconditions.checkNotNull(application);
		_application = application;
		
		// Add component listeners
		_mniFileExit.addActionListener(this);
		_mniSettingsDataModel.addActionListener(this);

		addComponents();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == _mniFileExit)
			_mniFileExit_ActionPerformed(e);
		else if(source == _mniSettingsDataModel)
			_mniSettingsDataModel_ActionPerformed(e);
	}
	
	private void _mniFileExit_ActionPerformed(ActionEvent e) {
		_application.closeStrattaFrame();
	}
	
	private void _mniSettingsDataModel_ActionPerformed(ActionEvent e) {
		_application.showSettingsDialog(SettingsDialog.DATAMODEL_TAB);
	}

	private void addComponents() {
		add(buildFileMenu());
		add(buildSettingsMenu());
	}
	
	private JMenu buildFileMenu() {
		JMenu mnuFile = new JMenu("File");
		
		mnuFile.add(_mniFileExit);
		
		return mnuFile;
	}
	
	private JMenu buildSettingsMenu() {
		JMenu mnuSettings = new JMenu("Settings");
		
		mnuSettings.add(_mniSettingsDataModel);
		
		return mnuSettings;
	}
}
