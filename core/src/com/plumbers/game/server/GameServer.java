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
                String level = "winter.tmx";
                RemoteClient client2 = new RemoteClient(accepted);
                waiting.ready(level, client2.getMyCharacter(), client2.myQueue);
                client2.ready(level, waiting.getMyCharacter(), waiting.myQueue);
                System.out.println("Starting thread 1");
                new Thread(waiting).start();
                System.out.println("Starting thread 2");
                new Thread(client2).start();
                System.out.println("go()");
                waiting.go();
                client2.go();
                
                waiting = null;
            }
        }
    }
}
