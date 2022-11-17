import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class ChatBot implements Runnable {
    private Socket serverSocket;
    private String name;
    private ArrayList<String> responses;

    public ChatBot(String address, int portNo) {
        this.name = "Bot";
        this.responses = new ArrayList<String>();
        //Initialises the array of responses that the bot can send
        this.responses.add("Hi");
        this.responses.add("Goodbye");
        this.responses.add("Just look out the window");
        this.responses.add("The future, the present, and the past walked into a bar. Things got a little tense");
        this.responses.add("I'm sorry I didn't understand that");
        try {
            serverSocket = new Socket(address, portNo);
        //Ends the code if the server isn't running
        } catch (IOException e) {
            System.out.println("Server is inactive");
            System.exit(0);
        }
    }

    public void startBot(String command){
        try{
            PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            //Sends responses depending on the command sent by a client
            switch (command){
                case "greet":
                    serverOut.println(this.name + ": " + this.responses.get(0));
                    break;
                case "bye":
                    serverOut.println(this.name + ": " + this.responses.get(1));
                    break;
                case "weather":
                    serverOut.println(this.name + ": " + this.responses.get(2));
                    break;
                case "joke":
                    serverOut.println(this.name + ": " + this.responses.get(3));
                    break;
                case "exit":
                    //Ends the code if you type exit into the terminal
                    serverOut.println(this.name + ": exit");
                    System.exit(0);
                default:
                    serverOut.println(this.name + ": " + this.responses.get(4));
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void run(){
        while(true){
            try{
                if (serverSocket != null){
                    BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                    String serverResponse = serverIn.readLine();
                    //Stops the thread if it has been disconnected
                    if (serverResponse == null){
                        break;
                    }
                    String lowerResponse = serverResponse.toLowerCase();
                    //Removes infinite loop where it constantly prints null to the terminal
                    for(int i = 0; i < 1; i++){
                        System.out.println(serverResponse);
                    }
                    //Ends the code if the server shuts down
                    if (serverResponse.equals("Server has shut down")){
                        System.exit(0);
                    //Tells the bot to send a response
                    }else if (serverResponse.contains(": bot")){
                        if (lowerResponse.contains("what is the weather")){
                            this.startBot("weather");
                        } else if (lowerResponse.contains("tell me a joke")){
                            this.startBot("joke");
                        } else if (lowerResponse.contains("hi")){
                            this.startBot("greet");
                        } else if (lowerResponse.contains("bye")){
                            this.startBot("bye");
                        } else{
                            this.startBot("noUnderstand");
                        }
                    }
                }
            //Ends the code if you try to connect to a server that isn't running
            }catch(IOException e){
                System.out.println("The Server is inactive");
                break;
            }
        }
    }


    public static void main(String[] args){
        //Initialises default port and address
        int ccp = 14001;
        String cca = "localhost";
        //Allows user to choose a port and/or address to connect to
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
        ChatBot bot = new ChatBot(cca, ccp);
        Thread listen = new Thread(bot);
        listen.start();
        try{
            //Checks for an exit command in the terminal
            BufferedReader readExit = new BufferedReader(new InputStreamReader(System.in));
            String disconnect = readExit.readLine().toLowerCase();
            if (disconnect.equals("exit")){
                bot.startBot("exit");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
