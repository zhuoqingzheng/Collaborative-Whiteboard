
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class retrieves a reference to the remote object from the RMI registry. It
 * invokes the methods on the remote object as if it was a local object of the type of the 
 * remote interface.
 *
 */

public class Client {

    public static void main(String[] args) {

        try {
            //Connect to the rmiregistry that is running on localhost
            // localhost == 172.30.48.1
            //WhiteBoard myBoard;

            Registry registry = LocateRegistry.getRegistry("172.30.48.1");

            //Retrieve the stub/proxy for the remote math object from the registry

            IRemoteBoard remoteBoard = (IRemoteBoard) registry.lookup("JoinBoard");
            WhiteBoard whiteBoard = new WhiteBoard();

            ClientRemote clientRemote = new ClientRemote("jojo",whiteBoard, remoteBoard);
            //whiteBoard.setClientRemote(clientRemote);
            remoteBoard.addClient(clientRemote);

            Canvas iniCanvas;




        }catch(Exception e) {
            e.printStackTrace();
        }

    }

}