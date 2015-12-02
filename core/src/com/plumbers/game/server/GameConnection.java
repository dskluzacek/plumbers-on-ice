package com.plumbers.game.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.Player;
import com.plumbers.game.server.EventMessage.MsgType;

public class GameConnection
{
    private final BlockingQueue<Message> outgoing = new LinkedBlockingQueue<Message>();
    private Player localPlayer;
    private RemotePlayer remotePlayer;
    
    private String levelFile, myCharName, oppCharName; 
    
    private Socket socket;
    private Scanner input;
    private Writer out;
    
    public GameConnection(String hostname, int port, String charName) throws IOException
    {
        this.myCharName = charName;
//        SocketHints hints = new SocketHints();
//        hints.performancePrefBandwidth = 0;
//        hints.performancePrefConnectionTime = 0;
//        hints.performancePrefLatency = 1;
//      hints.tcpNoDelay = true;
        
        try {
            socket = Gdx.net.newClientSocket(Protocol.TCP, hostname, port, null);
        } catch (GdxRuntimeException ex) {
            throw new IOException("failed to connect to server at: "
                                  + hostname + ':' + port, ex);
        }
        input = new Scanner( socket.getInputStream() );
        out = new OutputStreamWriter( socket.getOutputStream() );
    }
    
    public void handshake() throws IOException {
        out.write( myCharName );
        out.write("\n");
        out.flush();
        
        String[] arr = input.nextLine().split(" ");
        levelFile = arr[0].trim();
        oppCharName = arr[1].trim();
    }
    
    public void ready() {
        input.nextLine();
        
        Thread writerThread = new Thread( new MessageWriter() );
        Thread readerThread = new Thread( new MessageReader() );
        writerThread.start();
        readerThread.start();
    }
    
    public void setPlayers(Player local, RemotePlayer rp) {
        remotePlayer = rp;
        localPlayer = local;
    }
    
    public String getLevelFileName() {
        return levelFile;
    }
    
    public String getOppCharacterName() {
        return oppCharName;
    }
    
    public void enqueue(Message m) {
        outgoing.add(m);
    }

    private class MessageWriter implements Runnable {
        @Override
        public void run() {
            StringBuilder builder = new StringBuilder();
            Message m;
            
            while ( socket.isConnected() ) {
                try {
                    m = outgoing.take();
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
    
    private class MessageReader implements Runnable {
        @Override
        public void run() {
            StateMessage state;
            EventMessage event;
            
            while ( socket.isConnected() ) {
                try {
                    String type = input.next();
                    
                    if ( type.equals("state") ) {
                        state = StateMessage.obtain();
                        state.read(input);
                        remotePlayer.putStateMessage(state);
                    } else {
                        event = EventMessage.obtain();
                        event.read(input);
                        
                        if ( event.getType() == MsgType.COIN
                                && event.appliesToLocalPlayer() )
                        {
                            localPlayer.incrementCoins();
                        }
                        remotePlayer.pushEventMessage(event);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            
        }
    }
    
}
