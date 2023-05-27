import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/**
 * Server side implementation of the remote interface.
 * Must extend UnicastRemoteObject, to allow the JVM to create a
 * remote proxy/stub.
 *
 */
public class RemoteBoard extends UnicastRemoteObject implements IRemoteBoard {

    private WhiteBoard board;
    private ArrayList<IClientRemote> clients = new ArrayList<>();
    protected RemoteBoard() throws RemoteException {

    }

    @Override
    public void addClient(IClientRemote client) throws  RemoteException{
        clients.add(client);
        System.out.println("Num of Clients: " + clients.size());
    }
    @Override
    public void deleteClient(IClientRemote client) throws  RemoteException{
        clients.add(client);
        for (IClientRemote elem: clients){
            if (elem.getUsername().equals(client.getUsername())){
                clients.remove(elem);
            }
        }
        System.out.println("Num of Clients: " + clients.size());
    }

    @Override
    public  void addShape(Shape shape, Color color){
        try {
            for (IClientRemote client: clients){

                client.updateClient(shape, color);


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void addText(String text, Point position, Color color){
        try{
            for (IClientRemote client: clients){
                client.receiveText(text, position, color);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}