package com.plumbers.game.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.server.EventMessage.MsgType;

public class RemoteClient implements Runnable {
    private Socket socket;
    private Scanner input;
    private Writer out;
    protected BlockingQueue<Message> oppositeQueue;
    protected BlockingQueue<Message> myQueue = new LinkedBlockingQueue<Message>();
    private ServerMessageReader reader;
    private ServerMessageWriter writer;
    
    private String level, myCharacter, otherCharacter;
    
    private volatile boolean start = false;
    
    public RemoteClient(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new Scanner( socket.getInputStream() );
        this.out = new OutputStreamWriter( socket.getOutputStream() );
        
        this.myCharacter = input.next();
        
        reader = new ServerMessageReader();
        writer = new ServerMessageWriter();
    }
    
    public String getMyCharacter() {
        return myCharacter;
    }
    
    public void ready(String level, String otherCharacter, BlockingQueue<Message> oppositeQueue) {
        this.oppositeQueue = oppositeQueue;
        this.level = level;
        this.otherCharacter = otherCharacter;
    }
    
    public void go() {
        start = true;
    }
    
    @Override
    public void run() {
        Thread write, read;
        
        try {
            out.write(level);
            out.write(' ');
            out.write(otherCharacter);
            out.write("\n");
            out.flush();
            
            write = new Thread( writer );
            read = new Thread( reader );
            
            while (! start) {
            }
            
            out.write("NOW\n");
            out.flush();
            
            write.start();
            read.start();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
    }

    private class ServerMessageWriter implements Runnable {
        @Override
        public void run() {
            StringBuilder builder = new StringBuilder();
            Message m;
            
            while ( socket.isConnected() ) {
                try {
                    m = myQueue.take();
                    m.write(builder);
                    out.append(builder);
                    out.flush();
                    Pools.free(m);
                }
                catch (InterruptedException e) {
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    
    private class ServerMessageReader implements Runnable {
        @Override
        public void run() {
            StateMessage stateMsg;
            EventMessage eventMessage;
            
            while ( socket.isConnected() ) {
                try {
                    String type = input.next();
                    
                    if ( type.equals("state") ) {
                        stateMsg = StateMessage.obtain();
                        stateMsg.read(input);
                        oppositeQueue.add(stateMsg);
                    } else {
                        eventMessage = EventMessage.obtain();
                        eventMessage.read(input);
                        
                        if ( eventMessage.getType() == MsgType.COIN )
                        {
//                          game.coinEvent(eventMessage);
                            continue;
                        }
                        oppositeQueue.add(eventMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            
        }
    }
    
}
