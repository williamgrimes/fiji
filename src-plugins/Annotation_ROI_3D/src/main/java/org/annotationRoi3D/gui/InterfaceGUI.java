package org.annotationRoi3D.gui;

import ij.*;
import ij.gui.*;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import mcib3d.geom.*;
import mcib3d.image3d.ImageHandler;

import org.annotationRoi3D.gui.EventAction;
import org.annotationRoi3D.methods.RoiDisplay;

import javax.swing.JCheckBox;

/**
 * This class initialises the GUI interface for the Annotation ROI 3D plug-in, 
 * the layout is defined here. Java swing elements can be manipulated using
 * a window builder. 
 *
 * @author William Grimes
 * @version 1.0.0
 */
public class InterfaceGUI extends JFrame implements PlugIn {

	private static final long serialVersionUID = -7508668331230924418L;
	public static ImageHandler ima;
	public static ImagePlus imp;
	public static ImagePlus currentImage;
	public static ImagePlus plus;
	public static ImagePlus activeImage;
	public static int middle;
	protected static Roi[] arrayRois;
	public static Object3D obj;
	protected static Objects3DPopulation objects3D;
	protected static DefaultListModel<Object> model = new DefaultListModel<Object>();
	protected static HashMap<Object, Object> hash;
	static boolean live = true;
	public static int[] indexes;
	public static int count;
	protected ResultsTable rtVoxels;
	public static String roiLabel = "null";
	public static double volume = Double.NaN;
	protected static int currentZmin = 0;
	protected static int currentZmax = 0;
	protected static int roiCmX = 0;
	protected static int roiCmY = 0;
	protected static int roiCmZ = 0;
	protected static String comment = "Add comments here ...";
	public JPanel jPanel = new JPanel();
	public static boolean identified = false;
	public Border border = new EtchedBorder(EtchedBorder.LOWERED);
	protected static JTextPane RoiInformation; 
	protected static JEditorPane RoiComment; 
	protected static JCheckBox chckbxCheckBox;
	protected static JLabel lblImage;
	protected static AdjustmentListener al;
	protected static MouseWheelListener ml;
	public static JList<Object> list = new JList<Object>();

	/**
	 * Creates new Annotation_ROI_3D interface 
	 */
	public InterfaceGUI() {
		initComponents();
		this.setTitle("Annotation ROI 3D");
		model.clear();
		list.setModel(model);
		
		list.updateUI();

		setVisible(true);
		objects3D = new Objects3DPopulation();
		hash = new HashMap<Object, Object>();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Initialises components of GUI and defines layout.
	 */
	@SuppressWarnings("serial")
	private void initComponents() {
		jPanel.setLayout(null);        
		setResizable(false);

		JLabel lblRoiList = new JLabel("ROI List");
		lblRoiList.setBounds(10, 10, 60, 15);
		jPanel.add(lblRoiList);
		
		list.setFont(new Font("Dialog", Font.PLAIN, 12));
		list.setModel(new javax.swing.AbstractListModel<Object>() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
			public int getSize() { return strings.length; }
			public Object getElementAt(int i) { return strings[i]; }
		});

		list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				EventAction.listValueChanged(evt);
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setBounds(10, 30, 170, 370);
		jPanel.add(jScrollPane);
		jScrollPane.setViewportView(list);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(jPanel, GroupLayout.PREFERRED_SIZE, 475, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(jPanel, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
		);

		lblImage = new JLabel("Image");
		lblImage.setBounds(200, 10, 260, 15);
		jPanel.add(lblImage);

		JButton btnOpen = new JButton("Open");
		btnOpen.setToolTipText("Open metaimage (.mhd) files");
		btnOpen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnOpen.setBounds(200, 30, 120, 25);
		btnOpen.setBorder(border);
		jPanel.add(btnOpen);
		btnOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonOpenActionPerformed(evt);
			}
		});

		JButton btnIdentifyROI = new JButton("Identify ROI");
		btnIdentifyROI.setToolTipText("Identify ROIs within image and add to list");
		btnIdentifyROI.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnIdentifyROI.setBounds(340, 30, 120, 25);
		btnIdentifyROI.setBorder(border);
		jPanel.add(btnIdentifyROI);
		btnIdentifyROI.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonIdentifyROIActionPerformed(evt);
			}
		});

		JLabel lblChannels = new JLabel("Channels");
		lblChannels.setBounds(200, 65, 70, 15);
		jPanel.add(lblChannels);

		JButton btnMergeChannels = new JButton("Merge");
		btnMergeChannels.setToolTipText("<html>Merge colour channels and create a composite; <br> set C4 as scan image and C1 as overlay</html>");
		btnMergeChannels.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnMergeChannels.setBounds(200, 85, 120, 25);
		btnMergeChannels.setBorder(border);
		jPanel.add(btnMergeChannels);
		btnMergeChannels.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonMergeChannelsActionPerformed(evt);
			}
		});

		JLabel lblViews = new JLabel("View");
		lblViews.setBounds(200, 120, 70, 15);
		jPanel.add(lblViews);

		JButton btnViewChannels = new JButton("View");
		btnViewChannels.setToolTipText("View and hide channels");
		btnViewChannels.setBounds(340, 85, 120, 25);
		btnViewChannels.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnViewChannels.setBorder(border);
		jPanel.add(btnViewChannels);
		btnViewChannels.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonViewChannelsActionPerformed(evt);
			}
		});

		JButton btnOrthogonalViews = new JButton("Orthogonal");
		btnOrthogonalViews.setToolTipText("Orthogonal view display of selected stack");
		btnOrthogonalViews.setBounds(200, 140, 120, 25);
		btnOrthogonalViews.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnOrthogonalViews.setBorder(border);		
		jPanel.add(btnOrthogonalViews);
		btnOrthogonalViews.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonOrthogonalViewsActionPerformed(evt);
			}
		});

		JButton btnView3D = new JButton("3D");
		btnView3D.setToolTipText("Project in 3D as rotating volume");
		btnView3D.setBounds(340, 140, 120, 25);
		btnView3D.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnView3D.setBorder(border);	
		jPanel.add(btnView3D);
		btnView3D.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonView3DActionPerformed(evt);
			}
		});
		
		JLabel lblInformation = new JLabel("ROI Information");
		lblInformation.setBounds(200, 180, 120, 15);
		jPanel.add(lblInformation);

		RoiInformation = new JTextPane();
		RoiInformation.setToolTipText("<html>Displays information about selected ROI:<br>"
				+ "ROI: name and coordinate positions (x, y and z)<br>"
				+ "Volume: the ROI volume in voxels<br>"
				+ "ZMin & ZMax: the minimum and maximum slice of ROI</html>");
		RoiInformation.setContentType("text/html");
		RoiInformation.setBorder(border);
		RoiInformation.setBounds(200, 200, 260, 80);
		RoiInformation.setEditable(false);  
		RoiInformation.setCursor(null);  
		RoiInformation.setOpaque(false);  
		RoiInformation.setFocusable(false);
		RoiInformation.setText(
				"<html><table align=\"center\" style = \"font-family: Dialog; font-size: 12; color: #333333\">" +					
						"<tr>"
						+ "<td><b>ROI: </b></td><td>"+roiLabel+"</td>"
						+ "</tr>"+
						"</table>"
						+ "<table align=\"center\" style = \"font-family: Dialog; font-size: 12; color: #333333\">" +
						"<tr>"
						+ "<td><b>Volume: </b></td><td>"+volume+"</td>"
						+ "</tr>"
						+"</table>"						
						+ "<table align=\"center\" style = \"font-family: Dialog; font-size: 12; color: #333333\">" +
						"<tr>"
						+ "<td><b>Zmin: </b></td><td>"+Integer.toString(currentZmin)+"</td><td><b>Zmax: </b></td><td>"+Integer.toString(currentZmax)+"</td>"
						+ "</tr>"+
						"</table></html>" 	
				);
		jPanel.add(RoiInformation); 

		JLabel lblComments = new JLabel("ROI Comment");
		lblComments.setBounds(200, 290, 125, 15);
		jPanel.add(lblComments);
		
		chckbxCheckBox = new JCheckBox("Checkbox");
		chckbxCheckBox.setBounds(370, 290, 125, 15);
		chckbxCheckBox.setEnabled(false);  
		jPanel.add(chckbxCheckBox);
		
		RoiComment = new JEditorPane();
		RoiComment.setEnabled(false);
		RoiComment.setBounds(200, 310, 260, 90); 
		RoiComment.setBorder(border);
		RoiComment.setText(comment);
		jPanel.add(RoiComment);
		
		JButton btnXmlImport = new JButton("Import XML");
		btnXmlImport.setToolTipText("Import data from XML file");
		btnXmlImport.setBounds(200, 405, 120, 25);
		btnXmlImport.setBorder(border);	
		jPanel.add(btnXmlImport);
		btnXmlImport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonXmlImportActionPerformed(evt);
			}
		});	

		JButton btnXmlExport = new JButton("Export XML");
		btnXmlExport.setToolTipText("Export data to XML file");
		btnXmlExport.setBounds(340, 405, 120, 25);
		btnXmlExport.setBorder(border);	
		jPanel.add(btnXmlExport);
		btnXmlExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EventAction.buttonXmlExportActionPerformed(evt);
			}
		});	

		getContentPane().setLayout(layout);

		pack();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * This method  listens for adjustments in the JList 
	 * and implements updateRoi method.
	 */
	public void adjustmentValueChanged(AdjustmentEvent ae) {
		RoiDisplay.updateRois();
	}

	@Override
	public void run(String arg) {
		new InterfaceGUI().setVisible(true);
	}
}