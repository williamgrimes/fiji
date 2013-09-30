package org.annotationRoi3D.io;

 
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.annotationRoi3D.gui.InterfaceGUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 * This class parses ROI comments into an XML file 
 * and streams the output, it is implemented by 
 * the FileChooser class.
 * 
 * @return Nothing.
 */
public class ExportXML extends InterfaceGUI{
	public static DOMSource source;
	public static Transformer transformer;
	
	public static void OutputXML() {
	 
	  try {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("ROIs");
		doc.appendChild(rootElement);
		
		for (int i = 0; i < objects3D.getNbObjects(); i++){
			//System.out.println(objects3D.getObject(i).toString());
			
			Element ROI = doc.createElement("ROI");
			rootElement.appendChild(ROI);
			
			//X-coordinate elements
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(objects3D.getObject(i).toString()));
			ROI.appendChild(name);
	 
			Element x = doc.createElement("x");
			x.appendChild(doc.createTextNode(String.valueOf(objects3D.getObject(i).getCenterX())));
			ROI.appendChild(x);
	 
			// Y-coordinate elements
			Element y = doc.createElement("y");
			y.appendChild(doc.createTextNode(String.valueOf(objects3D.getObject(i).getCenterY())));
			ROI.appendChild(y);
	 
			// Z-coordinate elements
			Element lastname = doc.createElement("z");
			lastname.appendChild(doc.createTextNode(String.valueOf(objects3D.getObject(i).getCenterZ())));
			ROI.appendChild(lastname);

			// checkbox elements
			Element checkbox = doc.createElement("checkbox");
			checkbox.appendChild(doc.createTextNode(String.valueOf(objects3D.getObject(i).getName().toString())));
			ROI.appendChild(checkbox);	
			
			// comment elements
			Element comment = doc.createElement("comment");
			comment.appendChild(doc.createTextNode(String.valueOf(objects3D.getObject(i).getComment().toString())));
			ROI.appendChild(comment);			
		}
 
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		StreamResult result = new StreamResult(new File(ExportFileChooser.ExportFile.getAbsolutePath()));
 
		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);
		//System.out.println("File saved!");
 
		transformer.transform(source, result);
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
}