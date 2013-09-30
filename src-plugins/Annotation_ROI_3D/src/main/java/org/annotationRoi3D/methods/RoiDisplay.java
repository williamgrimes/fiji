package org.annotationRoi3D.methods;

import java.awt.Frame;

import ij.gui.*;
import ij.IJ;
import ij.ImagePlus;
import ij.Macro;
import ij.gui.MessageDialog;
import ij.plugin.filter.ThresholdToSelection;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import mcib3d.image3d.ImageHandler;

import org.annotationRoi3D.gui.InterfaceGUI;

/**
 * This class contains all methods relating
 * to ROI display and updating.
 */
public class RoiDisplay extends InterfaceGUI{

	/**
	 * Shows selected Roi on image and defines
	 * parameters of selected Roi to be displayed
	 * in Roi information.
	 */
	public static void showRoi(boolean force, boolean all) {
		//ImageHandler ima = Image.getImage3D();
		//ImagePlus imp = Image.getImage();
		//Image.registerActiveImage();
		if (imp == null) {
			error("There are no images open.");
			return;
		}
		int zmin = imp.getNSlices() + 1;
		int zmax = -1;
		if (force) {
			// draw mask of rois
			indexes = list.getSelectedIndices();
			if ((indexes.length == 0) || all) {
				indexes = Image.getAllIndexes();
			}
			arrayRois = new Roi[imp.getNSlices()];
			// get zmin and zmax

			//Object3D obj = null;
			for (int i = 0; i < indexes.length; i++) {
				obj = objects3D.getObject(indexes[i]);
				if (obj.getZmin() < zmin) {
					zmin = obj.getZmin();
				}
				if (obj.getZmax() > zmax) {
					zmax = obj.getZmax();
				}
			}
			currentZmin = zmin;
			currentZmax = zmax;
			roiCmX = (int) obj.getMassCenterX(ima);
			roiCmY = (int) obj.getMassCenterY(ima);
			roiCmZ = (int) obj.getMassCenterZ(ima);
			volume = obj.getVolumeUnit();
			comment = obj.getComment();
			roiLabel = (String) model.get(indexes[0]);
			
			for (int zz = zmin; zz <= zmax; zz++) {
				IJ.showStatus("Computing Roi " + zz);
				ByteProcessor mask = new ByteProcessor(imp.getWidth(), imp.getHeight());
				for (int i = 0; i < indexes.length; i++) {
					obj = objects3D.getObject(indexes[i]);
					obj.draw(mask, zz, 255);
				}

				mask.setThreshold(1, 255, ImageProcessor.NO_LUT_UPDATE);
				ImagePlus maskPlus = new ImagePlus("mask " + zz, mask);

				ThresholdToSelection tts = new ThresholdToSelection();
				tts.setup("", maskPlus);
				tts.run(mask);

				arrayRois[zz] = maskPlus.getRoi();
			}
		}
		int middle = (int) (0.5 * zmin + 0.5 * zmax);
		imp.setSlice(middle + 1);
		imp.setZ(middle + 1);
		imp.setRoi(arrayRois[middle]);
		imp.updateAndDraw();
	}

	public static void updateRois() {
		// synchronized (this) {
		plus = Image.getImage();
		if (plus != null) {
			int sl = plus.getSlice() - 1;
			if ((sl >= currentZmin) && (sl <= currentZmax)) {
				plus.setRoi(arrayRois[sl]);
			} else {
				plus.killRoi();
			}
			plus.updateAndDraw();
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param msg Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean error(String msg) {
		Frame frame = null;
		new MessageDialog(frame, "ROI Manager", msg);
		Macro.abort();
		return false;
	}
}
