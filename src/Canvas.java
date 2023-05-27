import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.io.Serializable;

public class Canvas extends JPanel {
    private static final String RECTANGLE = "RECTANGLE";
    private static final String LINE = "LINE";
    private static final String OVAL = "OVAL";
    private static final String TEXT = "TEXT";
    private static final String CIRCLE = "CIRCLE";
    private IClientRemote clientRemote;
    private Point startPoint;
    private Point endPoint;
    private ArrayList<StoredShape> shapes;
    private Shape currentShape;
    private String currentTool;
    private Color currentColor;
    private TextNode tempText;
    private ArrayList<TextNode> texts;
    private String text;
    public Canvas() {

        if (clientRemote == null){
            System.out.println("********************123");
        }
        setPreferredSize(new Dimension(400, 300));
        currentColor = Color.BLACK;
        currentTool = "LINE";
        texts= new ArrayList<>();
        shapes = new ArrayList<>();
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                if (currentTool.equals(TEXT)) {
                    String textString = JOptionPane.showInputDialog("Input text:");

                    if (textString != null){
                        tempText = new TextNode(textString, startPoint,currentColor);
                        try {
                            clientRemote.sendText(tempText.text, startPoint,currentColor);
                        }
                        catch (Exception f){
                            f.printStackTrace();
                        }
                        texts.add(tempText);

                        repaint();
                    }

                }else {
                    createShape();
                }
                //System.out.println(testEnt.text);

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();

                repaint();


            }

            @Override
            public void mouseReleased(MouseEvent e) {


                    endPoint = e.getPoint();
                    createShape();

                    startPoint = null;
                    endPoint = null;
                    repaint();
                    shapes.add(new StoredShape(currentShape, currentColor));
                    try{
                        if (currentShape != null){
                            System.out.println(clientRemote);
                            clientRemote.updateServer(currentShape, currentColor);
                        }
                    }catch (Exception f){
                        f.printStackTrace();
                    }

                }


        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        //g2d.drawString("Hello", startPoint.x, startPoint.y);



            if (currentShape != null) {
                g2d.setColor(currentColor);
                //System.out.println("LOLOLOL");

                //drawResizingShape(g2d, startPoint, endPoint);
                g2d.draw(currentShape);
                g2d.fill(currentShape);



            }
            for (StoredShape shape : shapes){
                g2d.setColor(shape.getColor());
                g2d.draw(shape.getShape());
                g2d.fill(shape.getShape());
            }

            if (tempText != null){

                g2d.drawString(tempText.text, tempText.position.x,tempText.position.y);
            }

            for (TextNode text: texts){
                g2d.setColor(text.color);
                g2d.drawString(text.text,text.position.x, text.position.y);
             }

            if (startPoint != null && endPoint != null) {
                g2d.setColor(currentColor);
                drawResizingShape(g2d, startPoint, endPoint);
            }

    }

    private void drawResizingShape(Graphics2D g2d, Point start, Point end) {


        Shape shape;



        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        int diameter = Math.min(width, height);
        shape = new Line2D.Double(start,end);
        if (currentTool.equals(RECTANGLE)){
            shape = new Rectangle2D.Double(x, y, width, height);

        }
        if (currentTool.equals(OVAL)){
            shape = new Ellipse2D.Double(x, y, width, height);
        }
        if (currentTool.equals(CIRCLE)){
            shape = new Ellipse2D.Double(x,y,diameter,diameter);
        }
        if (currentTool.equals(LINE)){
            shape = new Line2D.Double(start,end);
            //g2d.draw(shape);
        }




        g2d.draw(shape);
        g2d.fill(shape);

    }

    private void createShape() {
        if (startPoint == null || endPoint == null) return;

        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);
        int diameter = Math.min(width, height);
        if (width > 0 && height > 0) {
            if (currentTool.equals(RECTANGLE)) {
                currentShape = new Rectangle2D.Double(x, y, width, height);


            }
            else if (currentTool.equals(LINE)){
                currentShape = new Line2D.Double(startPoint,endPoint);
            }
            else if (currentTool.equals(OVAL)){

                currentShape = new Ellipse2D.Double(x, y, width, height);

            }
            else if(currentTool.equals(CIRCLE)){
                currentShape = new Ellipse2D.Double(x, y, diameter,diameter);
            }else {
                currentShape = new Ellipse2D.Double(x, y, diameter,diameter);
            }
        }
    }

    public void setClientRemote(IClientRemote clientRemote) {
        System.out.println("set client remote");
        System.out.println(clientRemote);
        this.clientRemote = clientRemote;
    }

    public void setTool(String tool){
        currentTool = tool;
        System.out.println(currentTool);
    }
    public void setColor(Color color){
        currentColor = color;
        //System.out.println(currentColor);
    }
    public void draw(String s){
        texts.add(new TextNode(s,new Point(100,100),Color.CYAN));
        repaint();
    }

    public void addShape(Shape shape, Color color){

        shapes.add(new StoredShape(shape,color));
        System.out.println("00000000000000000000");
        System.out.println(shape);
        repaint();
    }
    public void addText(String text, Point position, Color color){

        texts.add(new TextNode(text,position,color));
        System.out.println("Text ADDED");

        repaint();
    }

    public void setCanvas(ArrayList<StoredShape> shapes, ArrayList<TextNode> texts){
        this.shapes = shapes;
        this.texts = texts;
        repaint();
    }

    public ArrayList<StoredShape> getShapes(){
        return shapes;
    }
    public ArrayList<TextNode> getTexts(){
        return texts;
    }

    public class StoredShape implements Serializable{

        private Shape shape;
        private Color color;

        private StoredShape(Shape shape, Color color){
            this.shape = shape;
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public Shape getShape() {
            return shape;
        }
    }
    public static class TextNode implements Serializable{
        String text;
        Point position;
        Color color;

        TextNode(String text, Point position, Color color) {
            this.text = text;
            this.position = position;
            this.color = color;
        }
    }


}