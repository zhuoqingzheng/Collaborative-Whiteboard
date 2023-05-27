import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private JTextArea chatHistory;
    private JTextField chatInput;
    private JButton submitButton;
    private IClientRemote clientRemote;
    private BorderLayout layout;
    private JScrollPane scrollPane;

    public ChatPanel() {
        layout = new BorderLayout();
        setLayout(layout);

        init();

    }

    private void init() {
        chatHistory = new JTextArea(12,28);
        chatHistory.setEditable(false);

        chatInput = new JTextField();

        //chatHistory.setPreferredSize(new Dimension(100,200));
        chatInput.setPreferredSize(new Dimension(100,5));


        submitButton = new JButton("Submit");
        submitButton.setMargin(new Insets(3,3,3,3));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        scrollPane = new JScrollPane(chatHistory,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(chatInput, BorderLayout.CENTER);
        southPanel.add(submitButton, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
    }



    private void sendMessage() {

        String message = chatInput.getText();
        if (!message.equals("")) {
            try{
                clientRemote.sendChat(message);

            }catch (Exception e){
                e.printStackTrace();
            }


            chatInput.setText("");

            //scrollPane = new JScrollPane(chatHistory,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        }
    }

    public void updateChat(String msg){
        chatHistory.append(msg);
    }

    public void setClientRemote(IClientRemote clientRemote) {
        this.clientRemote = clientRemote;
    }
}