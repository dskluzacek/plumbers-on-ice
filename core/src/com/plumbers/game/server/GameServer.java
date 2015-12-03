package com.plumbers.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    static ServerSocket socket;
    
    public static void main(String[] args) throws IOException {
        socket = new ServerSocket(7684); 
                
        Socket accepted;
        RemoteClient waiting = null;
        
        while ( (accepted = socket.accept()) != null )
        {
            if (waiting == null) {
                waiting = new RemoteClient(accepted);
            } else {
                String level = "tropical.tmx";
                RemoteClient client2 = new RemoteClient(accepted);
                waiting.ready(level, client2.getMyCharacter(), client2.myQueue);
                client2.ready(level, waiting.getMyCharacter(), waiting.myQueue);
                new Thread(waiting).start();
                new Thread(client2).start();
                waiting.go();
                client2.go();
                
                waiting = null;
            }
        }
    }
}
