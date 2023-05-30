import javax.swing.*;
import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IClientRemote extends Remote {

        public void updateServer(Shape shape, Color color) throws RemoteException;

        public void updateClient(Shape shape, Color color) throws RemoteException;

        public void sendText(String text, Point position, Color color) throws RemoteException;

        public void receiveText(String text, Point position, Color color) throws RemoteException;

        public String getUsername() throws RemoteException;

        public void sendChat(String meg) throws RemoteException;

        public void updateChat(String newMsg) throws RemoteException;


        public boolean getIsAdmin() throws RemoteException;

        public Canvas getCanvas() throws RemoteException;

        public ArrayList<Canvas.StoredShape> getShapes(String roomId) throws RemoteException;

        public ArrayList<Canvas.TextNode> getTexts(String roomId) throws RemoteException;

        public void shutdown() throws RemoteException;

        public void updateUserList(ArrayList<String> list) throws RemoteException;

        public JTextArea getUserList() throws RemoteException;

        public void setUserList(String text) throws RemoteException;

        public void leave() throws RemoteException;

        public void kickUser(String username, String roomId) throws RemoteException;

        public void getKicked() throws RemoteException;
        public void close() throws RemoteException;
        public String getRoomId() throws RemoteException;
        public void roomClosed() throws RemoteException;

        public boolean request(String roomId) throws RemoteException;

        public boolean handleRequest(String username) throws RemoteException;

        public void clearBoard() throws RemoteException;
        public void getClearedBoard() throws RemoteException;
}
