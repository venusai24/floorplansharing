package com.floorplannersharing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class SketchPanel extends JPanel{
    private DrawingTool drawingTool;
    // private RectManager rectManager;
    private CanvasObjectManager objectManager = CanvasObjectManager.getInstance();
    private static final int GRID_SIZE = 16; // size in pixels
    private boolean snapEnabled = true;
    private boolean gridEnabled = true;
    private int offsetX, offsetY; // offset when dragging an object
    private JPopupMenu selectMenu;
    private JPopupMenu alignmentMenu;
    private JPopupMenu directionMenu;
    CanvasObject finishedObject;
    CanvasObject copiedObject;
    CanvasObject clonedObject;
    CanvasObject staticObject;
    CanvasObject selectedObject;
    CanvasObject copyObject;
    String[] positioning = new String[2];
    private JLabel statusLabel;
    private boolean relativePositionLock = false;
    private boolean relativeAlignmentLock = false;
    private boolean roomSelected = false;
    public static BufferedImage canvas = null;
    public static BufferedImage canvasOverlay = null;

    private final float[] dashPattern = {5, 5};
    private final Stroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
    private final Stroke plainStroke = new BasicStroke(2);

    public SketchPanel(DrawingTool DrawingTool) {
        this.drawingTool = DrawingTool;
        //this.statusLabel = statusLabel;
        
        // --------- Select Menu implementation ----------
        selectMenu = new JPopupMenu();
        alignmentMenu = new JPopupMenu();
        directionMenu = new JPopupMenu();
        JMenuItem left = new JMenuItem("Left"); 
        JMenuItem center = new JMenuItem("Center"); 
        JMenuItem right = new JMenuItem("Right"); 
        // selectMenu.setBounds(200,100,150,200);
        alignmentMenu.add(left);
        alignmentMenu.add(center);
        alignmentMenu.add(right);

        JMenuItem rotateAntiClockwise = new JMenuItem("Rotate Anti-Clockwise");
        JMenuItem rotateClockwise = new JMenuItem("Rotate Clockwise");
        JMenuItem delete = new JMenuItem("Delete");
        JMenu addRoom = new JMenu("Add a Room");
        JMenuItem align = new JMenuItem("Align");
        JMenuItem north = new JMenuItem("North");
        JMenuItem south = new JMenuItem("South");
        JMenuItem east = new JMenuItem("East");
        JMenuItem west = new JMenuItem("West");
        JMenuItem north1 = new JMenuItem("North");
        JMenuItem south1 = new JMenuItem("South");
        JMenuItem east1 = new JMenuItem("East");
        JMenuItem west1 = new JMenuItem("West");
        addRoom.add(north); 
        addRoom.add(south);
        addRoom.add(east);
        addRoom.add(west);

        directionMenu.add(north1); 
        directionMenu.add(south1);
        directionMenu.add(east1);
        directionMenu.add(west1);

        // ------ Select Menu functionalities ------------
        rotateAntiClockwise.addActionListener((ActionEvent e) -> {
            // copiedObject = (CanvasObject)finishedObject.clone();
            finishedObject.rotate();
            if (rotateCollision(finishedObject)) {
                System.out.println("Collision detected on ROTATE");
                
                CanvasObjectManager.getInstance().removeObject(finishedObject);
                objectManager.addObject(copiedObject);
            } else {
                finishedObject.rotate();
                RotationUtility.RotateWithContained(objectManager, finishedObject, -90);
            }
            repaint();
            // redrawBuffer();

        });

        rotateClockwise.addActionListener((ActionEvent e) -> {
            finishedObject.rotate();
            if (rotateCollision(finishedObject)) {
                System.out.println("Collision detected on ROTATE");
                //statusLabel.setText("Intersection ERROR!");

                CanvasObjectManager.getInstance().removeObject(finishedObject);
                objectManager.addObject(copiedObject);
            } else {
                finishedObject.rotate();
                RotationUtility.RotateWithContained(objectManager, finishedObject, 90);
            }
            repaint();
            // redrawBuffer();
        });

        align.addActionListener((ActionEvent e) -> {
            updateStatusLabel("Select a Room to align with.");
            relativeAlignmentLock = true;
            repaint();
        });

        delete.addActionListener((ActionEvent e) -> {
            objectManager.removeObject(finishedObject);
            CanvasObjectManager.getInstance().removeObject(finishedObject);
            repaint();
            // redrawBuffer();
        });

        north.addActionListener((ActionEvent e) -> {
            positioning[0] = "N";
            updateStatusLabel("Select a Room.");
            relativePositionLock = true;
            repaint();
            // redrawBuffer();
        });

        north1.addActionListener((ActionEvent e) -> {
            positioning[0] = "N";
            alignmentMenu.show(this, selectedObject.x + selectedObject.width/2, selectedObject.y + selectedObject.height/2);
            repaint();
            // redrawBuffer();
        });

        south.addActionListener((ActionEvent e) -> {
            positioning[0] = "S";
            updateStatusLabel("Select a Room.");
            relativePositionLock = true;
            repaint();
            // redrawBuffer();
        });

        south1.addActionListener((ActionEvent e) -> {
            positioning[0] = "S";
            alignmentMenu.show(this, selectedObject.x + selectedObject.width/2, selectedObject.y + selectedObject.height/2);
            repaint();
            // redrawBuffer(); 
        });

        east.addActionListener((ActionEvent e) -> {
            positioning[0] = "E";
            updateStatusLabel("Select a Room.");
            relativePositionLock = true;
            repaint();
            // redrawBuffer();
        });

        east1.addActionListener((ActionEvent e) -> {
            positioning[0] = "E";
            alignmentMenu.show(this, selectedObject.x + selectedObject.width/2, selectedObject.y + selectedObject.height/2);
            repaint();
            // redrawBuffer();
        });

        west.addActionListener((ActionEvent e) -> {
            positioning[0] = "W";
            updateStatusLabel("Select a Room.");
            relativePositionLock = true;
            repaint();
            // redrawBuffer();
        });

        west1.addActionListener((ActionEvent e) -> {
            positioning[0] = "W";
            alignmentMenu.show(this, selectedObject.x + selectedObject.width/2, selectedObject.y + selectedObject.height/2);
            repaint();
            // redrawBuffer();
        });

        left.addActionListener((ActionEvent e) -> {
            positioning[1] = "L";
            updateStatusLabel("");
            repaint();
            // redrawBuffer();
            if(relativePositionLock){
                relativePlacement(positioning, staticObject, clonedObject);
                if(moveCollision(clonedObject)){
                    updateStatusLabel("Room Overlapping!!");
                }
                relativePositionLock = false;
                clonedObject = null;
                staticObject = null;
            }
            if(relativeAlignmentLock){
                relativeAlignment(positioning, selectedObject, staticObject);
                relativeAlignmentLock = false;
                selectedObject = null;
                staticObject = null;
                repaint();
            }
        });

        center.addActionListener((ActionEvent e) -> {
            positioning[1] = "C";
            updateStatusLabel("");
            repaint();
            // redrawBuffer();
            if(relativePositionLock){
                relativePlacement(positioning, staticObject, clonedObject);
                if(moveCollision(clonedObject)){
                    updateStatusLabel("Room Overlapping!!");
                }
                relativePositionLock = false;
                clonedObject = null;
                staticObject = null;
            }
            if(relativeAlignmentLock){
                relativeAlignment(positioning, selectedObject, staticObject);
                relativeAlignmentLock = false;
                selectedObject = null;
                staticObject = null;
                repaint();
            }
        });

        right.addActionListener((ActionEvent e) -> {
            positioning[1] = "R";
            updateStatusLabel("");
            repaint();
            // redrawBuffer();
            if(relativePositionLock){
                relativePlacement(positioning, staticObject, clonedObject);
                if(moveCollision(clonedObject)){
                    updateStatusLabel("Room Overlapping!!");
                }
                relativePositionLock = false;
                clonedObject = null;
                staticObject = null;
            }
            if(relativeAlignmentLock){
                relativeAlignment(positioning, selectedObject, staticObject);
                relativeAlignmentLock = false;
                selectedObject = null;
                staticObject = null;
                repaint();
            }
        });

        selectMenu.add(rotateClockwise);
        selectMenu.add(rotateAntiClockwise);
        selectMenu.add(delete);
        selectMenu.add(addRoom);

        add(selectMenu);
        // ---------- new code over --------------

        addMouseListener(new MouseAdapter() {
            // this is annonymous inner class
            @Override
            public void mousePressed(MouseEvent e) {
                drawingTool.startDrawing(snapToGrid(e.getX()), snapToGrid(e.getY()));
                if (drawingTool instanceof SelectTool) {
                    // create clone so that reference is not updated similar to finished object
                    if (drawingTool.getCurrentObject() != null) {
                        copiedObject = (CanvasObject)drawingTool.getCurrentObject().clone();
                    }
                }
                if(relativePositionLock){
                    staticObject = finishedObject;
                    if(objectManager.getObjectAt(e.getX(), e.getY()) instanceof Room){ 
                        clonedObject = (CanvasObject)objectManager.getObjectAt(e.getX(), e.getY()).clone();
                        System.out.println("Clicked"); // This is a debug line
                        alignmentMenu.show(e.getComponent(), e.getX(), e.getY());
                    }else{
                        updateStatusLabel("The component selected is not a room. Select a room.");
                        relativePositionLock = false;
                    }
                    
                }
                if(relativeAlignmentLock){
                    staticObject = finishedObject;
                    if(objectManager.getObjectAt(e.getX(), e.getY()) instanceof Room){ 
                        selectedObject = (CanvasObject)objectManager.getObjectAt(e.getX(), e.getY());
                        System.out.println("Clicked"); // This is a debug line
                        directionMenu.show(e.getComponent(), e.getX(), e.getY());
                        //alignmentMenu.show(e.getComponent(), e.getX(), e.getY());
                    }else{
                        updateStatusLabel("The component selected is not a room. Select a room.");
                        relativeAlignmentLock = false;
                    }
                    
                }
                repaint();
                // redrawBuffer();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!relativeAlignmentLock && !relativePositionLock){
                finishedObject = drawingTool.getCurrentObject();
                if (finishedObject != null) {
                    if (drawingTool instanceof SelectTool) {
                        if (moveCollision(finishedObject)) {
                             System.out.println("Collision detected on MOVE");
                            if (finishedObject instanceof Door || finishedObject instanceof Window) {
                                JOptionPane.showMessageDialog(null, "Cannot move Door or Window", "No move", JOptionPane.WARNING_MESSAGE, null);
                            } else {
                                JOptionPane.showMessageDialog(null, "Click OK to revert to previous position", "Overlap Detected", JOptionPane.OK_OPTION, null);
                            }
                            objectManager.removeObject(finishedObject);
                            objectManager.addObject(copiedObject);
                        } 
                        // else {
                        //     copiedObject = null;
                        // }
                        showPopup(e);
                    } else {
                        objectManager.addObject(finishedObject);
                    }
                }
                drawingTool.finishDrawing();
                repaint();
                // redrawBuffer();
            }
        }

            private void showPopup(MouseEvent e) {
                if(!(finishedObject instanceof Room)){
                    roomSelected = false;
                    selectMenu.remove(addRoom);
                    selectMenu.remove(align);
                }
                if(!roomSelected & (finishedObject instanceof Room)){
                    roomSelected = true;
                    selectMenu.add(addRoom);
                    selectMenu.add(align);
                }
                if (e.isPopupTrigger()) {
                    selectMenu.show(e.getComponent(),e.getX(), e.getY());
                }
            }

            
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            // this is annonymous inner class
            @Override
            public void mouseDragged(MouseEvent e) {
                drawingTool.continueDrawing(snapToGrid(e.getX()), snapToGrid(e.getY()));
                repaint();
                // redrawBuffer();
            }
        });

        // ------ NEW CODE ---------
        // for later, set dynamic width and height
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBufferedImage();
                repaint();
            }
        });
        // canvas = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
        // canvasOverlay = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    private void resizeBufferedImage() {
        int newWidth = getWidth();
        int newHeight = getHeight();

        if (newWidth > 0 && newHeight > 0) {
            BufferedImage newCanvas = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            BufferedImage newCanvasOverlay = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = newCanvas.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(canvas, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            Graphics2D g2dOverlay = newCanvasOverlay.createGraphics();
            g2dOverlay.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2dOverlay.drawImage(canvasOverlay, 0, 0, newWidth, newHeight, null);
            g2dOverlay.dispose();

            canvas = newCanvas;
            canvasOverlay = newCanvasOverlay;
        }
    }

    //We got the status label to put in collision and rotation detection
    public void getJLabel(JLabel statusLabel){
        this.statusLabel = statusLabel;
    }

    private void updateStatusLabel(String message) {
        if (statusLabel != null) {
            SwingUtilities.invokeLater(() -> statusLabel.setText(message));
            // statusLabel.setText(message);
        }
    }

    //Complete Overlap Function
    private boolean completeOverlapcheck(CanvasObject co){
        for(CanvasObject object : objectManager.getObjects()){
            if(!(co == object)){
                if(object.x == co.x && object.y == co.y && object.width == co.width && object.height == co.height){
                    return true;
                    
                }
            }
            } 
        return false;
    }


    // checking collision when moving objects around
    boolean moveCollision(CanvasObject selectedObject) {
        for (CanvasObject object: objectManager.getObjects()) {

            if (!object.equals(selectedObject) 
                && object.intersects(selectedObject) 
                && !(object instanceof Room && selectedObject instanceof Furniture && object.contains(selectedObject)) 
                && !(selectedObject instanceof Room && object instanceof Furniture && selectedObject.contains(object))
                //&& !(selectedObject instanceof Furniture  && (object instanceof Window ||  object instanceof Door ))
                //Add some condition such that it becomes false when furniture is selected
                &&  !(object instanceof Door && selectedObject.intersects(object) && selectedObject instanceof Room )
                
                ){
                
                updateStatusLabel("Collision on move");
                if ((object instanceof Door  && selectedObject instanceof Room ))
                    System.out.println("TRUE");

                return true;
            }
        }
        return false;
    }

    boolean checkCollision(CanvasObject selectedObject) {
        for (CanvasObject object: objectManager.getObjects()) {
            if (!object.equals(selectedObject) && object.intersects(selectedObject) &&
            !(object instanceof Room && selectedObject instanceof Furniture && object.contains(selectedObject)) 
            && !(selectedObject instanceof Room && object instanceof Furniture && selectedObject.contains(object)) || selectedObject.equals(object)) {
                
                updateStatusLabel("Collision on move");

                return true;
            }
        }
        return false;
    }

    boolean rotateCollision(CanvasObject selectedObject) {
        for (CanvasObject object: objectManager.getObjects()) {
            if( selectedObject != object 
                && !copiedObject.contains(object) 
                && selectedObject.intersects(object)
                && !(selectedObject instanceof Furniture)
                ){
                    updateStatusLabel("Collision on rotate");
                return true;
            }
        }
        return false;
    }

    // snap coordinates to the nearest grid point
    private int snapToGrid(int value) {
        if (snapEnabled) {
            return (value / GRID_SIZE) * GRID_SIZE;
        }
        return value;
    }

    // Undo, specific for Rectangle
    // public void undoLastPath() {
    //     pathManager.undoLastPath();
    //     repaint();
    // }

    // Clear Canvas of all rectangles
    // general clearCanvas method for all shapes yet to be implemented
    // public void clearCanvas() {
    //     rectManager.clearAllRects();
    //     repaint();
    // }

    // Change the drawing tool
    // this method is called by SketchApp, in SkethApp it is called by ToolPanel
    public void setDrawingTool(DrawingTool tool) {
        this.drawingTool = tool;
    }

    public DrawingTool getDrawingTool() {
        return this.drawingTool;
    }


    // -------Toggle Features Area------------
    
    // toggle snap function
    public void setSnapEnabled(boolean enabled) {
        snapEnabled = enabled;
    }

    // toggle grid
    public void setGridEnabled(boolean enabled) {
        gridEnabled = enabled;
        repaint();
        // redrawBuffer();
    }

    // ------------- Draw Methods ---------------
    // @Override
    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g); // don't know how it works, why it works
    //     Graphics2D g2d = (Graphics2D) g;

    //     if (gridEnabled) {
    //         // draw grid first then draw other things on top of it
    //         drawLineGrid(g2d);
    //         // Dot grid is commented
    //         // drawDotGrid(g2d);
    //     }
            
    //     g2d.setColor(Color.BLACK);
    //     g2d.setStroke(new BasicStroke(2)); // drawing rectangle stroke size

    //     // draw all those rectangles
    //     for (CanvasObject object: objectManager.getObjects()) {
    //         object.draw(g2d);
    //     }

    //     if (drawingTool.getCurrentObject() != null) {
    //         drawingTool.getCurrentObject().draw(g2d);
    //     }
    // }
    
    // --------Experimental code ----------
    private void redrawBuffer(Graphics2D g2d) {
        // Graphics2D g2d = (Graphics2D) g;
        Graphics2D canvasg2d = canvas.createGraphics();
        Graphics2D overlay = canvasOverlay.createGraphics();

        Composite prevComposite = canvasg2d.getComposite();

        canvasg2d.setComposite(AlphaComposite.Clear);
        canvasg2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        overlay.setComposite(AlphaComposite.Clear);
        overlay.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        canvasg2d.setComposite(prevComposite);
        overlay.setComposite(prevComposite);
        
        if (gridEnabled) {
            // draw grid first then draw other things on top of it
            drawLineGrid(canvasg2d);
            drawLineGrid(g2d);
            // Dot grid is commented
            // drawDotGrid(canvasg2d);
        }
            
        canvasg2d.setColor(Color.BLACK);
        canvasg2d.setStroke(new BasicStroke(2)); // drawing rectangle stroke size
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2)); // drawing rectangle stroke size
    

        // draw all those rectangles
        for (CanvasObject object: objectManager.getObjects()) {
            if (object instanceof Door || object instanceof Window) {
                paintOverBlack(object, overlay);
            }
             else {
                if (object instanceof Room) {
                    object.draw(canvasg2d);
                }
                object.draw(g2d);
            }
        }
    
        if (drawingTool.getCurrentObject() != null) {
            drawingTool.getCurrentObject().draw(g2d);
            drawingTool.getCurrentObject().draw(canvasg2d);
        }

        canvasg2d.dispose();
        overlay.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (canvas != null) {
            redrawBuffer(g2d);
    
            // g.drawImage(canvas, 0, 0, null);
            g2d.drawImage(canvasOverlay, 0, 0, null); // draws windows and doors
            
        }
    }

    private void paintOverBlack(CanvasObject object, Graphics2D overlay) {
        int minX = -1;
        int maxX = -1;
        int minY = -1;
        int maxY = -1;
        // for (int x = object.x + 1; x < object.x + object.width - 1; x++) {
        //     for (int y = object.y + 1; y < object.y + object.height - 1; y++) {
        //         int pixel = canvas.getRGB(x, y);
        //         if (pixel == Color.black.getRGB()) {
        //             canvasOverlay.setRGB(x, y, Color.white.getRGB());
        //         }
        //     }
        // }
        for (int x = object.x + 1; x < object.x + object.width - 1; x++) {
            int pixel = canvas.getRGB(x, object.y + 1);
            if (pixel == Color.black.getRGB()) {
                minX = maxX = x + 1;
                minY = object.y + 1;
                maxY = object.y + object.height - 1;
                break;
            }
        }
        if (minX == -1) {
            for (int y = object.y + 1; y < object.y + object.height - 1; y++) {
                int pixel = canvas.getRGB(object.x + 1, y);
                if (pixel == Color.black.getRGB()) {
                    minX = object.x + 1;
                    maxX = object.x + object.width - 1;
                    minY = maxY = y + 1;
                    break;
                }
            }
        }

        if (object instanceof Door) {
            overlay.setStroke(plainStroke);
        } else {
            overlay.setStroke(dashedStroke);
        }
        
        overlay.drawLine(minX, minY, maxX, maxY);
    }
    
    // --------- Experimental Code Ends here --------------

    // method for fixed grid
    private void drawLineGrid(Graphics2D g2d) {
        g2d.setColor(Color.lightGray);

        for (int x = 0; x < getWidth(); x += GRID_SIZE) {
            g2d.drawLine(x, 0, x, getHeight());
        }

        for (int y = 0; y < getHeight(); y += GRID_SIZE) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawDotGrid(Graphics g2d) {
        int ovalSize = 4;
        g2d.setColor(Color.lightGray);
        for (int y = 0; y < getHeight(); y += GRID_SIZE) {
            for (int x = 0; x < getWidth(); x += GRID_SIZE) {
                g2d.fillOval(x-ovalSize/2, y-ovalSize/2, ovalSize, ovalSize);
            }

        }

    }

    private void relativePlacement(String[] arr, CanvasObject initial, CanvasObject movable){
        if(arr[0].equals("N")){
            if(arr[1].equals("L")){
                movable.setBounds(initial.x,initial.y - movable.height, movable.width, movable.height);
            }
            if(arr[1].equals("R")){
                movable.setBounds(initial.x + initial.width-movable.width, initial.y - movable.height, movable.width, movable.height);
            }
            if(arr[1].equals("C")){
                movable.setBounds(initial.x + initial.width/2 -movable.width/2, initial.y - movable.height, movable.width, movable.height);
            }
        }
        if (arr[0].equals("S")) {
            if (arr[1].equals("L")) {
                movable.setBounds(initial.x, initial.y + initial.height, movable.width, movable.height);
            } else if (arr[1].equals("R")) {
                movable.setBounds(initial.x + initial.width - movable.width, initial.y + initial.height, movable.width, movable.height);
            } else if (arr[1].equals("C")) {
                movable.setBounds(initial.x + initial.width / 2 - movable.width / 2, initial.y + initial.height, movable.width, movable.height);
            }
        }
        
        if (arr[0].equals("E")) {
            if (arr[1].equals("L")) {
                movable.setBounds(initial.x + initial.width, initial.y, movable.width, movable.height);
            } else if (arr[1].equals("R")) {
                movable.setBounds(initial.x + initial.width, initial.y + initial.height - movable.height, movable.width, movable.height);
            } else if (arr[1].equals("C")) {
                movable.setBounds(initial.x + initial.width, initial.y + initial.height/2 - movable.height/2, movable.width, movable.height);
            }
        }
        
        if (arr[0].equals("W")) {
            if (arr[1].equals("R")) {
                movable.setBounds(initial.x - movable.width, initial.y, movable.width, movable.height);
            } else if (arr[1].equals("L")) {
                movable.setBounds(initial.x - movable.width, initial.y + initial.height - movable.height, movable.width, movable.height);
            } else if (arr[1].equals("C")) {
                movable.setBounds(initial.x - movable.width, initial.y + initial.height/2 - movable.height/2, movable.width, movable.height);
            }
        }
        if (!objectManager.getObjects().contains(movable)) {
            objectManager.addObject(movable);
        }else{
            statusLabel.setText("Collision on move");
        }
        revalidate();
        repaint();
        // redrawBuffer();
        
    }

    private void relativeAlignment(String[] arr, CanvasObject initial, CanvasObject movable){
        int initx = movable.x;
        int inity = movable.y;
        int initwidth = movable.width;
        int initheight = movable.height;
        if(arr[0].equals("N")){
            if(arr[1].equals("L")){
                movable.setBounds(initial.x,initial.y - movable.height, movable.width, movable.height);
            }
            if(arr[1].equals("R")){
                movable.setBounds(initial.x + initial.width-movable.width, initial.y - movable.height, movable.width, movable.height);
            }
            if(arr[1].equals("C")){
                movable.setBounds(initial.x + initial.width/2 -movable.width/2, initial.y - movable.height, movable.width, movable.height);
            }
        }
        if (arr[0].equals("S")) {
            if (arr[1].equals("L")) {
                movable.setBounds(initial.x, initial.y + initial.height, movable.width, movable.height);
            } else if (arr[1].equals("R")) {
                movable.setBounds(initial.x + initial.width - movable.width, initial.y + initial.height, movable.width, movable.height);
            } else if (arr[1].equals("C")) {
                movable.setBounds(initial.x + initial.width / 2 - movable.width / 2, initial.y + initial.height, movable.width, movable.height);
            }
        }
        
        if (arr[0].equals("E")) {
            if (arr[1].equals("L")) {
                movable.setBounds(initial.x + initial.width, initial.y, movable.width, movable.height);
            } else if (arr[1].equals("R")) {
                movable.setBounds(initial.x + initial.width, initial.y + initial.height - movable.height, movable.width, movable.height);
            } else if (arr[1].equals("C")) {
                movable.setBounds(initial.x + initial.width, initial.y + initial.height/2 - movable.height/2, movable.width, movable.height);
            }
        }
        
        if (arr[0].equals("W")) {
            if (arr[1].equals("R")) {
                movable.setBounds(initial.x - movable.width, initial.y, movable.width, movable.height);
            } else if (arr[1].equals("L")) {
                movable.setBounds(initial.x - movable.width, initial.y + initial.height - movable.height, movable.width, movable.height);
            } else if (arr[1].equals("C")) {
                movable.setBounds(initial.x - movable.width, initial.y + initial.height/2 - movable.height/2, movable.width, movable.height);
            }
        }
        
        if(moveCollision(movable) || completeOverlapcheck(movable)){
            updateStatusLabel("Collision on Alignment");
            movable.setBounds(initx, inity, initwidth, initheight);
            
            
        }
        // redrawBuffer();
        
    }

    
}
