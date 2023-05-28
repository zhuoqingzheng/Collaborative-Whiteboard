import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class WhiteBoard extends JFrame{
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JPanel panel2;
    private ChatPanel chatPanel;
    private ArrayList<String> userList;
    private JTextArea userListBoard;
    private Canvas canvas;
    private JPanel menuPanel;
    private String roomId;
    private ClientRemote clientRemote;
    private String currentTool;
    public WhiteBoard(){
        System.out.println("WhiteBoards" + clientRemote);
        canvas = new Canvas();
        initialize();
    }



    public void initialize(){
        setTitle("WhiteBoard");
        userList = new ArrayList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        bottomPanel = new JPanel();
        topPanel = new JPanel();
        chatPanel = new ChatPanel();
        panel2 = new JPanel();

        BoxLayout boxLayout3 = new BoxLayout(chatPanel,BoxLayout.Y_AXIS);
        BoxLayout boxLayout = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        chatPanel.setLayout(boxLayout3);
        panel2.setLayout(boxLayout);
        menuPanel = createToolMenu();

        topPanel.setBackground(Color.lightGray);
        topPanel.setLayout(new FlowLayout());
        JButton btn1 = new JButton("New");
        JButton btn2 = new JButton("Save");
        JButton btn3 = new JButton("SaveAs");
        JButton btn4 = new JButton("Open");
        JButton btn5 = new JButton("Kick");

        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Kick User:");
                if (username != null){
                    try {
                        clientRemote.kickUser(username,roomId);
                    }catch (Exception c){
                        c.printStackTrace();
                    }

                }
            }
        });


        JButton btn6 = new JButton("Close");

            btn6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        clientRemote.leave();
                        System.exit(0);
                    }catch (Exception x){
                        x.printStackTrace();
                    }

                }
            });



        topPanel.add(btn1);
        topPanel.add(btn2);
        topPanel.add(btn3);
        topPanel.add(btn4);
        topPanel.add(btn5);
        topPanel.add(btn6);

        userListBoard = new JTextArea(15,20);


        bottomPanel.setSize(100,500);
        bottomPanel.setBackground(Color.CYAN);


        JTextField chatBox = new JTextField(30);
        userListBoard.setEditable(false);
        userListBoard.append("Current Users: \n");



        for (int i = 1; i<= 4; i++){
            JButton button = new JButton("Button " + Integer.toString((i)));
            button.setFocusable(false);
            button.setMargin(new Insets(3,3,3,3));
            panel2.add(button);
            panel2.add(Box.createVerticalStrut(10));

        }

        bottomPanel.add(userListBoard);
        bottomPanel.add(chatPanel);
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(10);
        borderLayout.setVgap(10);
        setLayout(borderLayout);
        add(new JButton("NORTH"), BorderLayout.NORTH);
        //add(new JButton("WEST"), BorderLayout.WEST);
        //add(new JButton("EAST"), BorderLayout.EAST);
        //add(new JButton("SOUTH"), BorderLayout.SOUTH);
        //add(new JButton("CENTER"), BorderLayout.CENTER);

        //panel.setSize(100,500);
        add(topPanel,BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
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

    public void setCanvas(ArrayList<Canvas.StoredShape> shapes, ArrayList<Canvas.TextNode> texts){
            canvas.setCanvas(shapes,texts);
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

    public void getKicked(){
        System.exit(0);
    }
    public Canvas getCanvas() {
        return canvas;
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public void updateChat(String msg){
        chatPanel.updateChat(msg);
    }

    public String getRoomId(){
        return roomId;
    }

    public void shutdown() {
        System.exit(0);
    }
    public void updateUserList(ArrayList<String> list){
        userList = list;
        String text = "";
        for (String item: list){
            text = text + item + "\n";
        }
        userListBoard.setText(text);
    }
    public void addUser(String username){
        userListBoard.append(username + "\n");
        System.out.println("2:" + userListBoard.getText());
    }

    public JTextArea getUserListBoard() {
        System.out.println("3:" + userListBoard.getText());
        return userListBoard;
    }

    public void setUserList(String text){
        System.out.println("Seted" + text);
        userListBoard.setText(text);
    }
}
