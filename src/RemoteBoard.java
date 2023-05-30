import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.lang.Thread;


/**
 * Server side implementation of the remote interface.
 */
public class RemoteBoard extends UnicastRemoteObject implements IRemoteBoard {

    private ArrayList<WhiteBoard> boards;
    private HashMap<String,ArrayList<String>> rooms;

    private ArrayList<IClientRemote> clients = new ArrayList<>();
    protected RemoteBoard() throws RemoteException {
        rooms = new HashMap<>();

    }

    /** add a new client to the arraylist clients and hashmap<roomId, usernames in the room > rooms,  **/
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

                cli.updateUserList(users);
                if (cli.getUsername().equals(users.get(0))){

                    client.setUserList(cli.getUserList().getText());
                }

            }

        }


        clients.add(client);


    }

    /** after a client closes the whiteboard or get kicked out, delete the client **/
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

                    for (IClientRemote elem: clients){

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

                clientIter.remove();
            }
        }
        for (IClientRemote c: newClients){

            kickUser(c.getUsername(),c.getRoomId());
        }


    }
    /** update clients in the same room with new drawings **/
    @Override
    public  void addShape(Shape shape, Color color, String roomId){
        try {
            ArrayList<String> usernames = rooms.get(roomId);

            for (IClientRemote client: clients){
                if (usernames.contains(client.getUsername())){
                    client.updateClient(shape, color);
                }



            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    /** update clients in the same room with new texts **/
    @Override
    public void addText(String text, Point position, Color color,String roomId){
        try{
            ArrayList<String> usernames = rooms.get(roomId);
            for (IClientRemote client: clients){
                if (usernames.contains(client.getUsername())){
                    client.receiveText(text, position, color);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** update clients in the same room with new chat **/
    @Override
    public void addChat(String username, String msg,String roomId){
        try{
            String newMsg = username + ": " + msg + "\n";
            ArrayList<String> usernames = rooms.get(roomId);
            for (IClientRemote client: clients){
                if (usernames.contains(client.getUsername())) {
                    client.updateChat(newMsg);
                }
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

            if ((client.getUsername()).equals(username)){

                return true;
            }
        }
        return false;
    }


    @Override
    public Boolean checkUserExistInRoom(String username,String roomId) throws RemoteException{

        ArrayList<String> usernames = rooms.get(roomId);

        return usernames.contains(username);


    }
    /** kick out a user from room and server **/
    @Override
    public void kickUser(String username, String roomId) throws  RemoteException{

        try{
            Iterator<IClientRemote> clientIter = clients.iterator();
            while (clientIter.hasNext()) {
                IClientRemote item = clientIter.next();

                if (username.equals(item.getUsername())) {


                    ArrayList<String> usernames = rooms.get(item.getRoomId());
                    usernames.remove(username);
                    for (IClientRemote client: clients){

                        if (usernames.contains(client.getUsername())){
                            client.updateUserList(usernames);
                        }
                    }
                    rooms.put(item.getRoomId(),usernames);
                    clientIter.remove();
                    /** notify the kicked client **/
                    item.getKicked();
                }
            }


        }catch (Exception e){

            e.printStackTrace();
        }


    }

    @Override
    public ArrayList<Canvas.StoredShape> getShapes(String roomId) throws RemoteException{
        String admin = rooms.get(roomId).get(0);

        for (IClientRemote client: clients){
            if (client.getUsername().equals(admin)){

                return client.getShapes(roomId);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Canvas.TextNode> getTexts(String roomId) throws RemoteException{
        String admin = rooms.get(roomId).get(0);

        for (IClientRemote client: clients){
            if (client.getUsername().equals(admin)){

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
    /** if the server terminated, shut down all rooms **/
    @Override
    public void shutdown() throws RemoteException{
        ArrayList<Thread> threads = new ArrayList<>();
        //create threads for each operation to avoid errors
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

    /** shut down a room if the manager left **/
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
                    //rooms.remove(roomId);

                } catch (RemoteException e) {

                }
            });

            t.start();
            threads.add(t);
        }




    }

    /** join request handling **/
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

    public void clearBoard(String roomId) throws RemoteException{
        ArrayList<String> usernames = rooms.get(roomId);
        for (IClientRemote client: clients){
            if (usernames.contains(client.getUsername())){
                client.getClearedBoard();
            }
        }
    }

    public void deleteRoom(String roomId,String username) throws RemoteException{

        IClientRemote admin = null;
        for(IClientRemote client: clients){
            if (username.equals(client.getUsername())){
                admin = client;
                break;
            }
        }
        if (admin!=null){
            clients.remove(admin);
        }

        rooms.remove(roomId);
    }



}