import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteBoard extends Remote {

    public void addClient(IClientRemote client) throws RemoteException;
    public void deleteClient(IClientRemote client) throws RemoteException;
    public void addShape(Shape shape, Color color) throws  RemoteException;
    public void addText(String text, Point position, Color color) throws  RemoteException;

}
