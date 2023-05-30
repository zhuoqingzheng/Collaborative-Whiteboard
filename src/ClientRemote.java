import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.*;
import java.rmi.server.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Client side implementation of the remote interface.
 */

public class ClientRemote extends UnicastRemoteObject implements IClientRemote {
    private WhiteBoard whiteboard;  // a GUI class representing the whiteboard
    private String username;
    private IRemoteBoard remoteBoard;
    private String roomId;

    private Boolean isAdmin;
    public ClientRemote(String username, WhiteBoard whiteboard, IRemoteBoard remoteBoard,Boolean isAdmin, String roomId) throws RemoteException {
        this.whiteboard = whiteboard;
        this.remoteBoard = remoteBoard;
        this.isAdmin = isAdmin;
        this.whiteboard.setClientRemote(this);
        this.whiteboard.getCanvas().setClientRemote(this);
        this.whiteboard.getChatPanel().setClientRemote(this);
        this.username = username;
        this.roomId = roomId;
        whiteboard.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

    }

    /** update the server that new shape is added **/
    @Override
    public void updateServer(Shape shape, Color color) throws RemoteException {
        //whiteboard.drawShape(s);
        remoteBoard.addShape(shape,color, roomId);

    }
    /** update client to get new shape from other users **/
    @Override
    public void updateClient(Shape shape, Color color) throws RemoteException{
        whiteboard.addShape(shape,color);
    }


    @Override
    public String getUsername() {
        return username;
    }

    /** send text **/
    @Override
    public void sendText(String text, Point position,Color color) throws RemoteException{
        remoteBoard.addText(text, position, color, roomId);
    }
    /** client receive new text (not chat) **/
    @Override
    public void receiveText(String text, Point position, Color color) throws RemoteException{
        whiteboard.addText(text, position, color);
    }
    /** client sends server a chat msg **/
    @Override
    public void sendChat(String msg) throws RemoteException{
        remoteBoard.addChat(username,msg,roomId);
    }
    /** client receive new msg **/
    @Override
    public void updateChat(String newMsg){
        whiteboard.updateChat(newMsg);
    }

    @Override
    public boolean getIsAdmin() throws RemoteException{
        return isAdmin;
    }

    @Override
    public Canvas getCanvas() throws RemoteException{
        return whiteboard.getCanvas();
    }


    @Override
    public ArrayList<Canvas.StoredShape> getShapes(String roomId) throws RemoteException{
        return whiteboard.getCanvas().getShapes();
    }

    @Override
    public ArrayList<Canvas.TextNode> getTexts(String roomId) throws RemoteException{
        return whiteboard.getCanvas().getTexts();
    }

    @Override
    public void shutdown() throws RemoteException{
        JOptionPane.showMessageDialog(null,"Server is disconnected");
        whiteboard.shutdown();
        //System.exit(0);
    }

    @Override
    public void roomClosed() throws RemoteException{
        JOptionPane.showMessageDialog(null,"Manager Left, Room Closing");
        whiteboard.shutdown();
        //System.exit(0);
    }

    @Override
    public void updateUserList(ArrayList<String> list) throws  RemoteException{
        whiteboard.updateUserList(list);
    }


    @Override
    public JTextArea getUserList() throws RemoteException{
        return whiteboard.getUserListBoard();

    }

    @Override
    public void setUserList(String text) throws RemoteException{
        whiteboard.setUserList(text);
    }

    @Override
    public void leave() throws RemoteException{
        remoteBoard.deleteClient(username);
        //System.exit(0);
    };

    @Override
    public void kickUser(String username, String room) throws RemoteException{

        if (remoteBoard.checkUserExistInRoom(username,roomId)){
            /** assign threads so other operations are not blocked **/
            new Thread(
                    ()->{
                        try {
                            remoteBoard.kickUser(username, roomId);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
            ).start();

        }
    }

    /** manager handle join request **/
    @Override
    public boolean handleRequest(String username) throws RemoteException{
        boolean[] answers = new boolean[1];
        answers[0] = false;

            int result = JOptionPane.showConfirmDialog(null, "Accept User " + username + " to join?", "Join Request", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                answers[0] = true;
            }

        return answers[0];
    }

    /** client is kicked out of the room **/
    @Override
    public void getKicked() throws RemoteException{

        JOptionPane.showMessageDialog(null,"You are kicked out of the room: ");
        whiteboard.unshow();
        //whiteboard.getKicked();
        UnicastRemoteObject.unexportObject(this, true);
        ;System.exit(0);
    }


    @Override
    public String getRoomId() throws RemoteException{
        return roomId;
    }

    //@Override
    //public void unShow() throws RemoteException{
        //UnicastRemoteObject.unexportObject(this, true);
     //   whiteboard.setVisible(false);
    // }

    /** close the whiteboard **/
    @Override
    public void close() throws RemoteException{
        UnicastRemoteObject.unexportObject(this, true);
        System.exit(0);
    }

    /** client sends join request **/
    @Override
    public boolean request(String roomId) throws RemoteException{
        return remoteBoard.request(roomId, username);
    }

    /** clear the board for new board **/
    @Override
    public void clearBoard() throws RemoteException{
        remoteBoard.clearBoard(roomId);
    }

    @Override
    public void getClearedBoard() throws RemoteException{
        whiteboard.getCanvas().setBlank();
    }


}