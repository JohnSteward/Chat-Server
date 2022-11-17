import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements Runnable{
    private ServerSocket serverSocket;
    private int portNo;
    private Boolean shutDown = false;
    public ArrayList<Socket> users;

    private ChatServer(int portNo, ArrayList<Socket> users) {
        this.users = users;
        try{
            serverSocket = new ServerSocket(portNo);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void ShutDown(Thread t1){
        try{
            //Looks for an 'exit' input on the server, causing it to shut down
            BufferedReader exit = new BufferedReader(new InputStreamReader(System.in));
            String closeServer = exit.readLine();
            if (closeServer.toLowerCase().equals("exit")){
                new Thread(new Broadcast(users, "Server has shut down")).run();
                shutDown = true;
                System.out.println("Server has shut down");
                for (Socket user : users){
                    user.close();
                }
                t1.interrupt();
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }


    }

    public void run(){
        try{
            //Accept connection from a client while the server is running
            while(!shutDown){
                System.out.println("Listening for connection");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection found on: " + serverSocket.getLocalPort() + "; " + clientSocket.getPort());
                users.add(clientSocket);
                new ServerMessage(clientSocket, clientSocket.getPort(), this.users);
            }
        }catch(IOException e){
            System.out.println("");
        }finally{
            try{
                serverSocket.close();
            }catch(IOException e){
                System.out.println("");
            }
        }
    }

    public static void main(String[] args){
        int csp = 14001;
        //Sets up the server to host all clients
        if (args.length > 1){
            if (args[0].equals("-csp")){
                try {
                    csp = Integer.parseInt(args[1]);
                }catch(NumberFormatException e){
                    System.out.println("Invalid port number, default to 14001");
                }
            }
        }
        ChatServer server = new ChatServer(csp, new ArrayList<Socket>());
        Thread t1 = new Thread(server);
        t1.start();
        server.ShutDown(t1);
    }
}
