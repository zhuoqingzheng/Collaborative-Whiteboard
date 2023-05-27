import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Thread;


/**
 * Server side implementation of the remote interface.
 * Must extend UnicastRemoteObject, to allow the JVM to create a
 * remote proxy/stub.
 *
 */
public class RemoteBoard extends UnicastRemoteObject implements IRemoteBoard {

    private ArrayList<WhiteBoard> boards;
    private HashMap<String,ArrayList<String>> rooms;

    private ArrayList<IClientRemote> clients = new ArrayList<>();
    protected RemoteBoard() throws RemoteException {
        rooms = new HashMap<>();

    }

    @Override
    public void addClient(IClientRemote client,String roomId) throws  RemoteException{
        if (client.getIsAdmin()){
            ArrayList<String> newList = new ArrayList<>();
            newList.add(client.getUsername());

            rooms.put(roomId, newList);
        }
        else{
            ArrayList<String> users = rooms.get(roomId);
            users.add(client.getUsername());
            rooms.put(roomId,users);
        }

        clients.add(client);
        System.out.println("Num of Clients: " + clients.size());
        System.out.println("Keys: " + rooms.keySet());
        System.out.println("Keys: " + rooms.values());
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

    @Override
    public void addChat(String username, String msg){
        try{
            String newMsg = username + ": " + msg + "\n";
            for (IClientRemote client: clients){
                client.updateChat(newMsg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Boolean checkRoomIdExist(String id){
        return true;
    }

    @Override
    public Boolean checkUserExist(String username) throws RemoteException{
        for (IClientRemote client: clients){
            System.out.println("found: "+client.getUsername());
            if ((client.getUsername()).equals(username)){
                //System.out.println("found: "+client.getUsername());
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Canvas.StoredShape> getShapes(String roomId) throws RemoteException{
        String admin = rooms.get(roomId).get(0);
        System.out.println("Check: " + rooms.get(roomId).get(0));
        for (IClientRemote client: clients){
            if (client.getUsername().equals(admin)){
                System.out.println("Admin: " + admin);
                return client.getShapes(roomId);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Canvas.TextNode> getTexts(String roomId) throws RemoteException{
        String admin = rooms.get(roomId).get(0);
        System.out.println("Check: " + rooms.get(roomId).get(0));
        for (IClientRemote client: clients){
            if (client.getUsername().equals(admin)){
                System.out.println("Admin: " + admin);
                return client.getTexts(roomId);
            }
        }
        return null;
    }
    @Override
    public Boolean checkRoomExist(String roomId) throws RemoteException{
            if (rooms.containsKey(roomId)){
                return true;
            }
            return false;
    }

    @Override
    public void shutdown() throws RemoteException{
        ArrayList<Thread> threads = new ArrayList<>();

        for (IClientRemote client : clients) {
            Thread t = new Thread(() -> {
                try {
                    client.shutdown();
                } catch (RemoteException e) {

                }
            });

            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {

            }
        }
        //System.exit(0);

    }




}