import org.annotationRoi3D.gui.InterfaceGUI;

import ij.IJ;
import ij.ImageJ;
import ij.plugin.PlugIn;

/**
 * Annotation ROI 3D
 *
 * The entry point of 3D annotation plugin, a plugin to annotate
 * 3D ROIs in ImageJ. The project is undertaken at the National
 * Institute of Informatics in Tokyo to assist the National 
 * Institute of Genetics in providing feedback about ROIs relating
 * to mouse phenotypes. For each ROI comments can be added and a
 * classification.
 *
 * @author William Grimes
 * @version 1.0.0
 */
public class Annotation_ROI_3D implements PlugIn{

	/**
	 * Method for running plugin from fiji build
	 *
	 * To compile fiji navigate to the fiji directory and run the
	 * build script (./Build.sh) in linux. All plugins in 
	 * src-plugins folder will be included in the build.
	 *
	 * @param args unused
	 */	
	public void run(String arg) {
		new InterfaceGUI().setVisible(true);
	}
	
	/**
	 * This method shows a message in a dialog box
	 * titled message, about the program.
	 */
	public void showAbout() {
		IJ.showMessage("Annotation_ROI_3D", "Annotation ROI 3D is a plugin to add comments and analysis to 3D regions of interest");
	}

	/**
	 * Main method for debugging.
	 *
	 * For debugging, it is convenient to have a method that starts ImageJ, loads an
	 * image and calls the plugin, e.g. after setting breakpoints.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		new ImageJ();	// launches ImageJ		
		new InterfaceGUI().setVisible(true);
	}
}
