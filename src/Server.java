
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Creates an instance of the RemoteMath class and
 * publishes it in the rmiregistry
 *
 */
public class Server {
    private static IRemoteBoard remoteBoard;
    private static Registry registry;
    public static void main(String[] args)  {

        try {


            remoteBoard = new RemoteBoard();
            //Publish the remote object's stub in the registry under the name "Compute"

            registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));

            //registry.bind("MathCompute", remoteMath);
            registry.bind("JoinBoard",remoteBoard);
            System.out.println("Board Server Ready");
            unbind();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void unbind() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                remoteBoard.shutdown();
                System.out.println("lalalalala");
                registry.unbind("JoinBoard");
                UnicastRemoteObject.unexportObject(remoteBoard, true);
                System.out.println("Board Server shutdown successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}