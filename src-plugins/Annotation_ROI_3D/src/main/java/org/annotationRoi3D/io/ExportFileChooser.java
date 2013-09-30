package org.annotationRoi3D.io;

import java.io.*;
import java.awt.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This creates a dialog window for exporting 
 * and importing XML files.
 */
public class ExportFileChooser extends JPanel {

	private static final long serialVersionUID = 1L;
	public static File ExportFile;
    JFileChooser fcExport;

    public ExportFileChooser() {
        super(new BorderLayout());
        fcExport = new JFileChooser();
        
        int returnValExport = fcExport.showSaveDialog(ExportFileChooser.this);
        if (returnValExport == JFileChooser.APPROVE_OPTION) {
            ExportFile = fcExport.getSelectedFile();
            org.annotationRoi3D.io.ExportXML.OutputXML();
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frameExport = new JFrame("FileChooserExport");
        frameExport.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frameExport.add(new ExportFileChooser());

        //Display the window.
        frameExport.pack();
        frameExport.setVisible(true);
    }
}
