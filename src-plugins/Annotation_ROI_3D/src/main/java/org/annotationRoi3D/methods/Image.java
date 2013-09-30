package org.annotationRoi3D.methods;

import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.measure.Calibration;

import mcib3d.geom.Object3D;
import mcib3d.geom.Object3DVoxels;
import mcib3d.geom.Objects3DPopulation;
import mcib3d.geom.Voxel3D;
import mcib3d.image3d.ImageHandler;
import mcib3d.image3d.ImageInt;

import org.annotationRoi3D.gui.EventAction;
import org.annotationRoi3D.gui.InterfaceGUI;


/**
 * This class contains methods relating to Image processes 
 * used within Annotation_ROI_3D
 */
public class Image extends InterfaceGUI{
	/**
	 * Returns a reference to the active image
	 * or null if there isn't one.
	 * 
	 * @return imp image reference
	 */
	public static ImagePlus getImage() {
		imp = WindowManager.getCurrentImage();
		if (imp == null) {
			return null;
		} else {
			return imp;
		}
	}	
	 
	/**
	 * Returns a reference to 3D image
	 * 
	 * @return ImageHanlder
	 */
	public static ImageHandler getImage3D() {
		ImagePlus imp = getImage();
		return ImageHandler.wrap(imp);
	}

	/**
	 * Sets current image as active image
	 */
	public static void registerActiveImage() {
		activeImage = Image.getImage();

		if (activeImage != null && activeImage.getProcessor() != null && activeImage.getImageStackSize() > 1) {
			if (currentImage != null && currentImage.getWindow() != null && currentImage != activeImage) {
				EventAction.removeScrollListener(currentImage, al, ml);
				EventAction.removeScrollListener(currentImage, al, ml);
				currentImage.killRoi();
				currentImage.updateAndDraw();
				currentImage = null;
			}
			if (currentImage != activeImage) {
				//System.out.println("add listener:"+activeImage.getTitle());
				EventAction.addScrollListener(activeImage, al, ml);
				currentImage = activeImage;
			}
		}
	}   

	/**
	 * Adds a feature to the Image attribute of the RoiManager3D_ object
	 */
	public static void addImage() {
		plus = Image.getImage();
		ImagePlus contours;
		String title = plus.getTitle();
		objects3D.setCalibration(plus.getCalibration());
	
		ImageHandler seg = ImageHandler.wrap(plus);
		int min = (int) seg.getMinAboveValue(0);
		int max = (int) seg.getMax();
		// iterate in image  and constructs objects
		ArrayList<Voxel3D>[] objects = new ArrayList[max - min + 1];
		for (int i = 0; i < max - min + 1; i++) {
			objects[i] = new ArrayList<Voxel3D>();
		}
		float pix;
		for (int k = 0; k < seg.sizeZ; k++) {
			for (int j = 0; j < seg.sizeY; j++) {
				for (int i = 0; i < seg.sizeX; i++) {
					pix = seg.getPixel(i, j, k);
					if (pix > 0) {
						objects[(int) (pix) - min].add(new Voxel3D(i, j, k, pix));
					}
				}
			}
		}
		// ARRAYLIST
		ArrayList listObjects = new ArrayList();
		for (int i = 0; i < max - min + 1; i++) {
			if (!objects[i].isEmpty()) {
				listObjects.add(objects[i]);
			}
		}
		// add objects
		addListVoxels(listObjects, plus);
		list.updateUI();
	}
	
	/**
	 * 	List voxels in the image with values > threshold
	 * 
	 * @param list ArrayList of obj
	 * @param plus image added by addImage()
	 */
	public static void addListVoxels(ArrayList list, ImagePlus plus) {
		ArrayList<Voxel3D> objList;
		java.util.Iterator it = list.iterator();
		Object3D obj;
		Calibration cal = plus.getCalibration();
		int i = 1;
		ImageInt seg = ImageInt.wrap(plus);
		while (it.hasNext()) {
			// Object3D
			objList = (ArrayList<Voxel3D>) it.next();
			if (!objList.isEmpty()) {
				obj = new Object3DVoxels(objList);
				obj.setCalibration(cal);
				obj.setLabelImage(seg);
				obj.setName("false");
				obj.setComment(" ");
				obj.computeContours();
				if (obj.getAreaPixels() == 0) {
					IJ.log("area 0 " + obj);
				}
				// RoiManager
				addObject3D(obj);
				i++;
			}
		}
	}

	
	/**
	 * Adds object3D to model and updates list
	 * 
	 * @return nothing
	 * @param obj 3d object to be added
	 */
	public static void addObject3D(Object3D obj) {
		objects3D.addObject(obj);
		model.addElement(obj.toString().substring(11));
		list.updateUI();
		list.repaint();
		list.revalidate();
	}

	/**
	 * Gets the allIndexes attribute of the RoiManager object
	 *
	 * @return The allIndexes value
	 */
	public static int[] getAllIndexes() {
		int count = model.getSize();
		int[] indexes = new int[count];
		for (int i = 0; i < count; i++) {
			indexes[i] = i;
		}
		return indexes;
	}

}
