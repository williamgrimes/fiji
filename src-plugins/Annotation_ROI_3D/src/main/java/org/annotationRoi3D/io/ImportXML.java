package org.annotationRoi3D.io;

import ij.IJ;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.annotationRoi3D.gui.InterfaceGUI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Arrays;

public class ImportXML extends InterfaceGUI{
	/**
	 * This method imports comments from an XML
	 * file, comments are added to Objects3D 
	 * population. A check is performed to ensure 
	 * that the data sets match, this is performed
	 * by checking the name and coordinates of each
	 * ROI is the same.
	 * 
	 * @param importFile the file to be imported.
	 * @return Nothing.
	 */
	public static void ImportXML(File importFile){

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(importFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("ROI");

			String[] array0 = new String[model.getSize()];
			for (int i = 0; i < model.getSize(); i++){
				array0[i] = model.get(i).toString(); // creates array for identified ROIs
			}

			String[] array1 = new String[nList.getLength()];
			for (int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					array1[i] = eElement.getElementsByTagName("name").item(0).getTextContent().substring(11);
					//creates array for ROIs in XML file
				}
			}

			if(Arrays.equals(array0, array1) == false){
				IJ.showMessage("Import XML", "Data sets to not match");
			}
			if (Arrays.equals(array0, array1) == true){
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						objects3D.getObject(temp).setComment(eElement.getElementsByTagName("comment").item(0).getTextContent());
						objects3D.getObject(temp).setName(eElement.getElementsByTagName("checkbox").item(0).getTextContent());
						//sets the comment value and checkbox for identified ROIs
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}