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



public class ClientRemote extends UnicastRemoteObject implements IClientRemote {
    private WhiteBoard whiteboard;  // a GUI class representing the whiteboard
    private String username;
    private IRemoteBoard remoteBoard;
    public ClientRemote(String username, WhiteBoard whiteboard, IRemoteBoard remoteBoard) throws RemoteException {
        this.whiteboard = whiteboard;
        this.remoteBoard = remoteBoard;
        this.whiteboard.setClientRemote(this);
        this.whiteboard.getCanvas().setClientRemote(this);
        this.username = username;
        whiteboard.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

    }
    @Override
    public void updateServer(Shape shape, Color color) throws RemoteException {
        //whiteboard.drawShape(s);
        remoteBoard.addShape(shape,color);

    }

    @Override
    public void updateClient(Shape shape, Color color) throws RemoteException{
        whiteboard.addShape(shape,color);
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void sendText(String text, Point position,Color color) throws RemoteException{
        remoteBoard.addText(text, position, color);
    }

    @Override
    public void receiveText(String text, Point position, Color color) throws RemoteException{
        whiteboard.addText(text, position, color);
    }
}