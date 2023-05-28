
import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.server.UnicastRemoteObject;


public class CreateWhiteboard {
    private static Registry registry;
    private static IRemoteBoard remoteBoard;

    public static void main(String[] args) {

        try {
            //Connect to the rmiregistry that is running on localhost
            // localhost == 172.30.48.1
            //WhiteBoard myBoard;
            if (args.length == 4){
                String IPAdress = args[0];
                int port = Integer.parseInt(args[1]);
                String username = args[2];
                String roomID = args[3];
                registry = LocateRegistry.getRegistry(IPAdress,port);

                //Retrieve the stub/proxy for the remote math object from the registry
                remoteBoard = (IRemoteBoard) registry.lookup("JoinBoard");

                if (remoteBoard.checkRoomExist(roomID)){
                    JOptionPane.showMessageDialog(null,"Room Already Exist");
                    System.exit(0);
                }
                if (remoteBoard.checkUserExist(username)){
                    System.out.println("-----------------------");
                    JOptionPane.showMessageDialog(null,"Username Already Exist");
                    System.exit(0);
                }



                WhiteBoard whiteBoard = new WhiteBoard();

                ClientRemote clientRemote = new ClientRemote(args[2],whiteBoard, remoteBoard,true,roomID);

                //whiteBoard.setClientRemote(clientRemote);
                remoteBoard.addClient(clientRemote,roomID);
                unbind(roomID);
            }
            else{
                System.out.println("Wrong Command");
            }






        }catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Connection With Server Failed");
            e.printStackTrace();
        }

    }
    private static void unbind(String roomId) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("hahaha");
                remoteBoard.shutdownRoom(roomId);

                //registry.unbind("JoinBoard");
                //UnicastRemoteObject.unexportObject(remoteBoard, true);
                System.out.println("closed...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

}