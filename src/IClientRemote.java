import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientRemote extends Remote {
        public void updateServer(Shape shape, Color color) throws RemoteException;
        public void updateClient(Shape shape, Color color) throws RemoteException;

        public void sendText(String text, Point position, Color color) throws  RemoteException;
        public void receiveText(String text, Point position, Color color) throws RemoteException;
        public String getUsername() throws RemoteException;
}
