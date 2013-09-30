package org.annotationRoi3D.methods;

import org.annotationRoi3D.gui.InterfaceGUI;

import mcib3d.image3d.ImageHandler;
import mcib3d.image3d.ImageLabeller;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.measure.Calibration;

public class Segmentation extends InterfaceGUI {

	public static String title;
	static ImagePlus plus = Image.getImage();
	int low = 128;
	int high = 255;
	
    public static void setTitle() {
        title = plus.getTitle();
     }
    public String getTitle() {
        return title;
     }
    
    /**
     * Creates optional dialog box to set threshold
     * for segmentation, with default values: low 128 
     * and high 255.
     *
     * @param low The low threshold parameter
     * @param high The high threshold parameter
     */    
    public static void segmentation3D() {
        GenericDialog gd = new GenericDialog("Threshold 3D");
        gd.addNumericField("Low_Threshold", 128, 0);
        gd.addNumericField("High_Threshold", 255, 0);
        //gd.showDialog();
        int low = (int) gd.getNextNumber();
        int high = (int) gd.getNextNumber();

        segmentation3D(low, high);
    }


    /**
     * segmentation3D method identifies 3D
     * regions within image stack
     *
     * @param low The low threshold parameter
     * @param high The high threshold parameter
     */    
    public static void segmentation3D(int low, int high) {       
        plus.killRoi();
        setTitle();
        Calibration cal = plus.getCalibration();
        ima = ImageHandler.wrap(plus);
        ima = ima.threshold(low, false, true);
        ImageLabeller labels = new ImageLabeller(false);
        ima = labels.getLabels(ima);
        if (cal != null) {
            ima.setScale(cal.pixelWidth, cal.pixelDepth, cal.getUnits());
        }
        ima.show(title);
    }
}
