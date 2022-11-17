import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class ServerMessage implements Runnable{
    public Socket clientSocket;
    private int clientPort;
    public ArrayList<Socket> users;

    public ServerMessage(Socket clientSocket, int clientPort, ArrayList<Socket> users){
        this.clientSocket = clientSocket;
        this.clientPort = clientPort;
        this.users = users;
        new Thread(this).start();
    }

    public void run() {
        try{
            while (true){
                //Constantly looking for inputs from clients
                InputStreamReader clientCharStream = new InputStreamReader(this.clientSocket.getInputStream());
                BufferedReader clientIn = new BufferedReader(clientCharStream);
                String userIn = clientIn.readLine();
                int nameIndex = userIn.indexOf(":");
                String lowerUserIn = userIn.toLowerCase();
                //Sends the user input from the client to be broadcast to all clients
                new Thread(new Broadcast(users, userIn)).start();
                //Checks for a bot command from a client and sends it to the bot
                //to respond accordingly
                if (lowerUserIn.contains(": exit")){
                    //User leaving the chat
                    users.remove(this.clientSocket);
                    this.clientSocket.close();
                    String leaveMessage = userIn.substring(0, nameIndex) + " Has left the chat";
                    new Thread(new Broadcast(users, leaveMessage)).start();
                }



            }
        }catch(IOException e){
            System.out.println("Client has disconnected");
        }

    }
}