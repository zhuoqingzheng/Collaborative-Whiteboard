import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WhiteBoard extends JFrame{
    private JPanel panel;
    private JPanel panel2;
    private Canvas canvas;
    private JPanel menuPanel;
    private ClientRemote clientRemote;
    private String currentTool;
    public WhiteBoard(){
        System.out.println("WhiteBoards" + clientRemote);
        canvas = new Canvas();
        initialize();
    }



    public void initialize(){
        setTitle("Frame One");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        panel = new JPanel();
        panel2 = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel2, BoxLayout.Y_AXIS);

        panel2.setLayout(boxLayout);
        menuPanel = createToolMenu();




        panel.setSize(100,100);
        panel.setBackground(Color.CYAN);

        JTextArea textField = new JTextArea();
        JTextField chatBox = new JTextField(30);

        textField.setText("Hello It is me Hello It is meHello It is meHello It is meHello It is\n meHello\n It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is meHello \nIt is meHello It is meHello It is meHello It is meHello It is meHello It is meHello It is me");
        //textField.setColumns(100);
        textField.setPreferredSize(new Dimension(300,200));
        //textField.setSize(100,100);
        //textField.setMargin(new Insets(5,10,100,10));
        panel.add(textField);
        panel.add(chatBox);

        for (int i = 1; i<= 4; i++){
            JButton button = new JButton("Button " + Integer.toString((i)));
            button.setFocusable(false);
            button.setMargin(new Insets(3,3,3,3));
            panel2.add(button);
            panel2.add(Box.createVerticalStrut(20));
        }
        panel.add(panel2);
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(10);
        borderLayout.setVgap(10);
        setLayout(borderLayout);
        add(new JButton("NORTH"), BorderLayout.NORTH);
        //add(new JButton("WEST"), BorderLayout.WEST);
        //add(new JButton("EAST"), BorderLayout.EAST);
        //add(new JButton("SOUTH"), BorderLayout.SOUTH);
        //add(new JButton("CENTER"), BorderLayout.CENTER);

        panel.setSize(100,500);
        add(panel, BorderLayout.SOUTH);
        add(canvas, BorderLayout.CENTER);
        //add(panel2,BorderLayout.SOUTH);
        add(menuPanel,BorderLayout.WEST);

        setVisible(true);
    }



    private JPanel createToolMenu(){
        menuPanel = new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(menuPanel, BoxLayout.Y_AXIS);
        menuPanel.setLayout(boxLayout2);

        /** create tool buttons **/
        JButton rectangleButton = new JButton("Rectangle");
        rectangleButton.setMargin(new Insets(3,15,3,15));
        rectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setTool("RECTANGLE");
            }
        });

        JButton circleButton = new JButton("Circle");
        circleButton.setMargin(new Insets(3,27,3,27));
        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setTool("CIRCLE");
            }
        });


        JButton lineButton = new JButton("Line");
        lineButton.setMargin(new Insets(3,31,3,32));
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setTool("LINE");
            }
        });


        //lineButton.setPreferredSize(new Dimension(100,50));
        JButton ovalButton = new JButton("Oval");
        ovalButton.setMargin(new Insets(3,31,3,31));
        ovalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setTool("OVAL");
            }
        });



        JButton textButton = new JButton("Text");
        textButton.setMargin(new Insets(3,31,3,31));
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setTool("TEXT");
            }
        });


        JButton colorButton = new JButton("choose color");
        colorButton.setMargin(new Insets(3,6,3,6));
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
                if (newColor != null) {
                    canvas.setColor(newColor);
                    colorButton.setBackground(newColor);
                }
            }
        });


        colorButton.setFocusable(false);
        //ovalButton.setMargin(new Insets(3,31,3,31));

        menuPanel.add(rectangleButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(circleButton);
        menuPanel.add(Box.createVerticalStrut(5));

        menuPanel.add(lineButton);
        menuPanel.add(Box.createVerticalStrut(5));

        menuPanel.add(ovalButton);
        menuPanel.add(Box.createVerticalStrut(5));

        menuPanel.add(textButton);
        menuPanel.add(Box.createVerticalStrut(5));

        menuPanel.add(colorButton);


        menuPanel.setSize(30,100);
        menuPanel.setBackground(Color.gray);
        return menuPanel;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void addShape(Shape shape, Color color){
        canvas.addShape(shape,color);
    }

    public void addText(String text, Point position, Color color){
        canvas.addText(text,position, color);
    }

    public void setClientRemote(ClientRemote clientRemote){

        this.clientRemote = clientRemote;
    }
    public Canvas getCanvas() {
        return canvas;
    }
}
