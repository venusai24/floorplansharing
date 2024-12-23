package com.floorplannersharing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class SketchApp extends JFrame{
    private SketchPanel sketchPanel;
    private ToolPanel toolPanel;
    private JButton rectButton; // not implemented
    private JButton clearButton;
    private JButton undoRect;
    boolean snapEnabled = true;
    boolean gridEnabled = true;
    private JLabel statusLabel;
    private FileManager fileManager;

    public SketchApp() {
        setTitle("2D Floor Planner");
        // setSize(800, 600);
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Set size to screen resolution

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);


        

        // ----------- Initialising Area ---------------
        // the sidebat panel for switching between tools 
        toolPanel = new ToolPanel(this);
        // pass in RectTool, abstract class DrawingTool handles the implementation
        sketchPanel = new SketchPanel(new RoomTool()); 
        // sketchPanel = new SketchPanel(new FurnitureTool());
        
        //Calling this method before because I need instance of statusLabel to put into CanvasObejctManager
        createBottomBar();

        //Hopefully this gets me the same instance of objectManager as declared in SketchPanel
        CanvasObjectManager objectManager = CanvasObjectManager.getInstance();
        objectManager.getLabel(statusLabel);
        
        //Sending Label to SketchPanel for move collision detections
        sketchPanel.getJLabel(statusLabel);

        // file manager
        fileManager = new FileManager(this, sketchPanel);
        
        
        // ----------- Adding Area ---------------
        add(toolPanel, BorderLayout.WEST);
        add(sketchPanel, BorderLayout.CENTER); // display main sketch area in center

        // Adding clear canvas button
        // clearButton = new JButton("Clear Canvas");
        // clearButton.addActionListener(e -> sketchPanel.clearCanvas()); // how do lambda functions work??
        // add(clearButton, BorderLayout.SOUTH);

        // undoRect = new JButton("Undo Last Rectangle");
        // undoRect.addActionListener(e -> sketchPanel.undoLastRect());
        // add(undoRect, BorderLayout.EAST);

        // top menu bar
        createMenuBar();

        // bottom panel with toggle buttons
        

        setVisible(true);

    }

    // ToDo : make menuBar and BottomBar external classes
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        
        // idea : try adding Menu item directly to menu bar
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem closeFile = new JMenuItem("Close");

        // Functionality of these buttons comes here
        // will be implemented shortly


        //Implementing save feature
        saveFile.addActionListener(e -> fileManager.saveToFile());
        openFile.addActionListener(e -> fileManager.loadFromFile());

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(closeFile);




        //--------------------This is a different menu----------------------------- 
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        // undo function implemented here
        // 
        // will be implemented shortly

        editMenu.add(undoItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }

    private void createBottomBar() {
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        // toggle Grid Button
        JToggleButton gridToggle = new JToggleButton("Toggle Grid", gridEnabled);
        gridToggle.addActionListener(e -> {
            // how exactly does isSelected() work
            gridEnabled = gridToggle.isSelected();
            sketchPanel.setGridEnabled(gridEnabled);
            statusLabel.setText("Grid " + (gridEnabled ? "Enabled" : "Disabled"));
        });

        JToggleButton snapToggle = new JToggleButton("Toggle Snap", snapEnabled);
        snapToggle.addActionListener(e -> {
            snapEnabled = snapToggle.isSelected();
            sketchPanel.setSnapEnabled(snapEnabled);
            statusLabel.setText("Snap " + (snapEnabled? "Enabled" : "Disabled"));
        });

        //Adding status label
        statusLabel = new JLabel("Status: Ready");

        bottomBar.add(gridToggle);
        bottomBar.add(snapToggle);
        bottomBar.add(statusLabel);
        add(bottomBar, BorderLayout.SOUTH);
    }

    // method to change drawing tool
    // right now does nothing? Is it being called somewhere
    public void setDrawingTool(DrawingTool tool) {
        sketchPanel.setDrawingTool(tool);
        statusLabel.setText("Status: Ready");
    }

    // this is used in ToolPanel to set room colors and furniture objects
    public DrawingTool getDrawingTool() {
        statusLabel.setText("Status: Ready");
        return sketchPanel.getDrawingTool();
        
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(SketchApp::new);
    }
}
