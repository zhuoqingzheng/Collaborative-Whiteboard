import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteBoard extends Remote {

    public void addClient(IClientRemote client, String roomId) throws RemoteException;
    public void deleteClient(String username) throws RemoteException;
    public void addShape(Shape shape, Color color) throws  RemoteException;
    public void addText(String text, Point position, Color color) throws  RemoteException;
    public void addChat(String username, String msg) throws RemoteException;
    public Boolean checkRoomIdExist(String roomId) throws RemoteException;
    public ArrayList<Canvas.StoredShape> getShapes(String roomId) throws RemoteException;
    public ArrayList<Canvas.TextNode> getTexts(String roomId) throws RemoteException;
    public Boolean checkUserExist(String username) throws RemoteException;
    public Boolean checkRoomExist(String roomId) throws RemoteException;
    public void shutdown() throws RemoteException;
    public Boolean checkUserExistInRoom(String username,String roomId) throws RemoteException;
    public void kickUser(String username, String roomId) throws RemoteException;
}
