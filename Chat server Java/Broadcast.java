import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.net.Socket;

public class Broadcast extends Thread{
    public ArrayList<Socket> users;
    public String userIn;
    public Broadcast(ArrayList<Socket> users, String userIn){
        this.users = users;
        this.userIn = userIn;
    }

    public void run(){
            try{
                //Sends any client input to all clients, along with who sent it
                for (Socket user : users){
                    PrintWriter clientOut = new PrintWriter(user.getOutputStream(), true);
                    clientOut.println(this.userIn);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
    }

}
