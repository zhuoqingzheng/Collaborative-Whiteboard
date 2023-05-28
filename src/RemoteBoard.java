import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
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
            client.updateUserList(newList);
        }
        else{
            ArrayList<String> users = rooms.get(roomId);
            users.add(client.getUsername());
            rooms.put(roomId,users);
            for (IClientRemote cli: clients){
                System.out.println("11111111111111111");
                cli.updateUserList(users);
                if (cli.getUsername().equals(users.get(0))){
                    System.out.println("Admin content: " + cli.getUserList().getText());
                    client.setUserList(cli.getUserList().getText());
                }

            }

        }


        clients.add(client);

        System.out.println("Num of Clients: " + clients.size());
        System.out.println("Keys: " + rooms.keySet());
        System.out.println("Keys: " + rooms.values());
    }
    @Override
    public void deleteClient(String username) throws  RemoteException{
        Iterator<IClientRemote> clientIter = clients.iterator();
        ArrayList<IClientRemote> newClients = new ArrayList<>();

        while (clientIter.hasNext()) {
            IClientRemote item = clientIter.next();

            if (username.equals(item.getUsername())) {

                if(item.getIsAdmin()){
                    //item.unshow();
                    ArrayList<String> usernames = rooms.get(item.getRoomId());
                    System.out.println(usernames);
                    for (IClientRemote elem: clients){
                        System.out.println("Executed!");
                        if (usernames.contains(elem.getUsername()) && !elem.getIsAdmin()){
                                    newClients.add(elem);



                        }
                    }

                    rooms.remove(item.getRoomId());
                    item.close();
                }
                if(!item.getIsAdmin()){
                    ArrayList<String> usernames = rooms.get(item.getRoomId());
                    usernames.remove(username);
                    for (IClientRemote client: clients){
                        if (usernames.contains(client.getUsername())){
                            client.updateUserList(usernames);
                        }
                    }
                    rooms.put(item.getRoomId(),usernames);
                }

                //System.out.println("IN DEL, FOUND ROOMID" + item.getRoomId());
                clientIter.remove();
            }
        }
        for (IClientRemote c: newClients){
            System.out.println("11111111111111111111");
            kickUser(c.getUsername(),c.getRoomId());
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
    public Boolean checkUserExistInRoom(String username,String roomId) throws RemoteException{
        System.out.println("ROOM ID:" + roomId);
        ArrayList<String> usernames = rooms.get(roomId);
        System.out.println(usernames);
        return usernames.contains(username);


    }

    @Override
    public void kickUser(String username, String roomId) throws  RemoteException{
        System.out.println("NO");
        try{
            Iterator<IClientRemote> clientIter = clients.iterator();
            while (clientIter.hasNext()) {
                IClientRemote item = clientIter.next();

                if (username.equals(item.getUsername())) {
                    System.out.println("IN DEL, FOUND Clients NUM" + clients.size());

                    ArrayList<String> usernames = rooms.get(item.getRoomId());
                    usernames.remove(username);
                    for (IClientRemote client: clients){
                        System.out.println("Lewis Get In There!");
                        if (usernames.contains(client.getUsername())){
                            client.updateUserList(usernames);
                        }
                    }
                    rooms.put(item.getRoomId(),usernames);
                    clientIter.remove();
                    item.getKicked();
                }
            }


        }catch (Exception e){
            System.out.println(clients.size());
            e.printStackTrace();
        }


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
                    deleteClient(client.getUsername());
                } catch (RemoteException e) {

                }
            });

            t.start();
            threads.add(t);
        }


        //System.exit(0);

    }
    @Override
    public void shutdownRoom(String roomId) throws RemoteException{
        ArrayList<String> usernames = rooms.get(roomId);
        ArrayList<IClientRemote> clientList = new ArrayList<>();
        for (IClientRemote client: clients){
            if (usernames.contains(client.getUsername()) && !client.getIsAdmin()){
                clientList.add(client);
            }
        }


        ArrayList<Thread> threads = new ArrayList<>();

        for (IClientRemote c : clientList) {
            Thread t = new Thread(() -> {
                try {
                    c.roomClosed();
                    deleteClient(c.getUsername());
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


    }
    public boolean request(String roomId, String username) throws RemoteException{
        ArrayList<String> usernames = rooms.get(roomId);
        String adminName = usernames.get(0);
        for (IClientRemote client: clients){
            if (client.getUsername().equals(adminName)){
                return client.handleRequest(username);
            }
        }
        return false;
    }



}