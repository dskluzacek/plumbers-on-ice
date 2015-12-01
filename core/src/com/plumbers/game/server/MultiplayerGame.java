package com.plumbers.game.server;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.net.Socket;
import com.plumbers.game.*;

public class MultiplayerGame {
    private RemoteClient clientA;
    private RemoteClient clientB;
    private String levelString;
//    private Level level;
//    private Coin[][] coinArray;
//    private int[][][] coinRecords;
//    private List<int[]> recordList = new ArrayList<int[]>();
    private int gameTick;
    
    public MultiplayerGame(RemoteClient a, RemoteClient b, String level) {
        clientA = a;
        clientB = b;
        levelString = level;
    }
    
    public void start() {
        
        
    }
    
    public void stateEvent(int tick, RemoteClient caller) {
        synchronized (this) {
            if (tick > gameTick) {
                gameTick = tick;
            }
        }
    }
        
//        if (gameTick == tick) {
//            int me, other;
//            RemoteClient otherClient;
//            
//            if (caller == clientA) {
//                me = 0;
//                other = 1;
//                otherClient = clientB;
//            } else {
//                me = 1;
//                other = 0;
//                otherClient = clientA;
//            }
//            RemoteClient winner = null;
//            RemoteClient loser = null;
//            
//            synchronized (coinArray) {
//                for (int[] record : recordList) {
//                    int clientId = (record[0] > 0 ? 0 : 1);
//                    int recordTick = record[clientId];
//                    
//                    if (recordTick < tick - 30000) {
//                        if (clientId == 0) {
//                            winner = clientA;
//                            loser = clientB;
//                        } else {
//                            winner = clientB;
//                            loser = clientA;
//                        }
//                    }
//                }
//            }
//            EventMessage msg = EventMessage.obtain();
//            msg.coin(gameTick, true, col, row);
//            winner.myQueue.add(msg);
//            
//        }
    
//    public void coinEvent(EventMessage msg, RemoteClient caller) {
//        int col = msg.getColumn();
//        int row = msg.getRow();
//        
//        int[] record = coinRecords[col][row];
//        RemoteClient otherClient;
//        int me, other;
//        
//        if (caller == clientA) {
//            me = 0;
//            other = 1;
//            otherClient = clientB;
//        } else {
//            me = 1;
//            other = 0;
//            otherClient = clientA;
//        }
//        boolean decided = false, otherWins = false;
//        
//        synchronized (coinArray) {
//            if (coinArray[col][row] == null)
//                return;
//            
//            if ( record[other] != -1 ) {
//                decided = true;
//                
//                if ( record[other] <= msg.getTickBegin() ) {
//                    // other player got it first
//                    otherWins = true;
//                }
//            } else {
//                record[me] = msg.getTickBegin();
//                recordList.add(record);
//            }
//        }
//        
//        if (decided) {
//            msg.setAppliesToLocalPlayer(! otherWins);
//            caller.myQueue.add(msg);
//            
//            EventMessage msg2 = EventMessage.obtain();
//            msg2.coin(msg.getTickBegin(), otherWins, col, row);
//            otherClient.myQueue.add(msg2);
//        }
//    }
    
}
