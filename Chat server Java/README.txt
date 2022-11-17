In order for you to be able to run the client properly, you first need to start running the server so it can listen to connections from
incoming clients as you run more instances of the client.

In order to change the port that the server is bound to, when initially running the server code, in the program arguments, type "-csp"
then type the port number that you want it to bind to separated by a space. If you try to pass a port number that can't be parsed to an
integer, it will default to port 14001.

In order to change the port the client binds to, in the program arguments, type "-ccp" then the port number separated by a space. To
change the address that it binds to, type "-cca" then the IP address to bind to, separated by a space.
The client must be bound to the same port and IP addressas the server, otherwise it will result in an error and the client code will end.
If you input an argument for the port number that cannot be parsed to an integer, it will default to port 14001.

Once the client has begun running, the first thing it will do is ask you to input your name (If you want to remain anonymous, just hit
enter when it asks for your name, and your name will be registered as 'Anon'). The name will also be validated so you cannot include a
space or a colon in it.
After you have input your name, you will join the chat and will be able to send messages to and receive messages from all connected
clients. If a user types "exit", then that user will disconnect from the server and its code will stop running.
If you type exit on the server terminal, all clients will be disconnected and the server will shut down.

You can run the chat bot exactly like if you were running a client, but the class to run is called 'ChatBot'. You can use the optional
parameters in exactly the same way as the client, or if you don't add the optional parameters ir will default to localhost and 14001.
When you run the chat bot, it will then constantly listen for responses from client inputs to the server. If a client types "bot " then 
one of these commands: "hi", "goodbye"(or "bye"), "what is the weather?" or "tell me a joke", the bot will respond to all connected clients
accordingly. if you use the "bot " command and don't use one of those commands, it will respond with: "I'm sorry, I didn't understand that"
You can tell the bot to disconnect from the server by typing "exit" into the bot's console, just like with the client.