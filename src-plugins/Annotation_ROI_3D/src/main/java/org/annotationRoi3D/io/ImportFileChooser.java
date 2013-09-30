package org.annotationRoi3D.io;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportFileChooser extends JPanel {

	private static final long serialVersionUID = 1L;
	public static File ImportFile;
	JFileChooser fcImport;

	/**
	 * This creates a dialog window for exporting 
	 * and importing XML files.
	 */
	public ImportFileChooser() {
		super(new BorderLayout());
		fcImport = new JFileChooser();
		fcImport.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
		fcImport.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fcImport.setAcceptAllFileFilterUsed(true);

		int returnValImport = fcImport.showOpenDialog(ImportFileChooser.this);
		if (returnValImport == JFileChooser.APPROVE_OPTION) {
			ImportFile = fcImport.getSelectedFile();
			ImportXML.ImportXML(ImportFile);
		}    
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	public static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frameImport = new JFrame("FileChooserImport");
		frameImport.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	

		//Add content to the window.
		frameImport.add(new ImportFileChooser());

		//Display the window.
		frameImport.pack();
		frameImport.setVisible(true);
		}

}