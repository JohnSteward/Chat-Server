import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Thread{
    private Socket serverSocket;
    private Boolean valid = false;
    private String name;
    public Boolean leftChat = false;
    public ChatClient(String address, int portNo){
        try{
            serverSocket = new Socket(address, portNo);
            //Makes it so you cannot include a space or colon in your name
            while(!valid){
                System.out.println("What is your name? ");
                BufferedReader getName = new BufferedReader(new InputStreamReader(System.in));
                this.name = getName.readLine();
                if (!this.name.contains(":") && !this.name.contains(" ")){
                    valid = true;
                }else{
                    System.out.println("That name is invalid");
                }
            }
            //Allows the user to remain anonymous
            if (this.name.equals("")){
                this.name = "Anon";
            }

        } catch(IOException e){
            System.out.println("Server is inactive");
        }
    }

    public void go(Thread receive){
        try{

            //Set up ability to read user input
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            //Set up ability to send data to server
            PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);

            serverOut.println(this.name + " has joined the chat!");
            //Constantly look for input from a client
            while(!leftChat){
                String userInputString = userInput.readLine();
                serverOut.println(this.name + ": " + userInputString);
                if (userInputString.toLowerCase().equals("exit")){
                    try{
                        //Waits so it doesn't get blocked before it can exit
                        synchronized (this) {
                            this.wait(100);
                        }
                    }catch(InterruptedException e){
                        System.out.println("Interrupted");
                    }
                }
            }
        //Ends the code if it tries to connect to a server that isn't running
        }catch(IOException e){
            System.out.println("The server is inactive");
        }catch (NullPointerException e){
           System.out.print(" ");
           System.exit(0);
        }
    }

    public void run(){
        //Separate thread to look for response from the server and output,
        //so the client can send and receive concurrently
       while(true){
           try{
               if (serverSocket != null){
                   BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                   String serverResponse = serverIn.readLine();
                   //Removes infinite loop where it constantly prints null to the terminal
                   if (serverResponse == null){
                       leftChat = true;
                       break;
                   }
                   for(int i = 0; i < 1; i++){
                       System.out.println(serverResponse);
                   }
                   if (serverResponse.equals("Server has shut down")){
                       System.exit(0);
                   }
               }
           //Stops the thread if the server is shut down
           }catch(IOException e){
               System.out.println("The Server is inactive");
               break;
           }
       }
    }



    public static void main(String[] args) {
        //Creates a new client and sets up all of the functions
        //Passes default port and address if no args given
        String cca = "localhost";
        int ccp = 14001;
        //Checks for args and changes port and address accordingly
        if (args.length > 0){
            if (args[0].equals("-cca")){
                if (args.length >= 2) {
                    cca = args[1];
                }
            }else if (args[0].equals("-ccp")){
                if (args.length >= 2) {
                    try {
                        ccp = Integer.parseInt(args[1]);
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid port number, default to 14001");
                    }
                }
            }
            if (args.length == 4){
                if (args[0].equals("-cca")){
                    cca = args[1];
                    if (args[2].equals("-ccp")){
                        try {
                            ccp = Integer.parseInt(args[3]);
                        }catch(NumberFormatException e){
                            System.out.println("Not a valid port number, default to 14001");
                        }
                    }
                }else if (args[0].equals("-ccp")){
                    try {
                        ccp = Integer.parseInt(args[1]);
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid port number, default to 14001");
                    }
                    if (args[2].equals("-cca")){
                        cca = args[3];
                    }
                }
            }
        }
        ChatClient client = new ChatClient(cca, ccp);
        Thread receive = new Thread(client);
        receive.start();
        client.go(receive);
    }
}
