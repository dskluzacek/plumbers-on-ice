package com.plumbers.game.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.Vector;

public final class EventMessage implements Message, Poolable {
    private int id;
    private MsgType type;
    private int tickNumber;
    private int[] fields = new int[3];
    private float x, y;
    private String stringField;
    private static int nextId = 1;
    
    private EventMessage() { this.reset(); }
    
    public static EventMessage obtain() {
        return Pools.obtain(EventMessage.class);
    }
    
    public enum MsgType {
        COIN,
        DAMAGED,
        DIED,
        ENEMY_SPAWN;
    }
    
    @Override
    public void reset() {
        id = -1;
        tickNumber = -1;
        x = Float.NaN;
        y = Float.NaN;
        type = null;
        fields[0] = Integer.MIN_VALUE;
        fields[1] = Integer.MIN_VALUE;
        fields[2] = Integer.MIN_VALUE;
        stringField = null;
    }

    public void write(StringBuilder out) {
        out.setLength(0);
        out.append("event ");
        out.append( type.name() );
        out.append(' ');
        out.append(id);
        out.append('\n');
        out.append(tickNumber);

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != Integer.MIN_VALUE) {
                out.append(' ');
                out.append(fields[i]);
            }
        }

        if (! Float.isNaN(x) || ! Float.isNaN(y)) {
            out.append(' ');
            out.append(x);

            if (! Float.isNaN(y)) {
                out.append(' ');
                out.append(y);
            }
        }

        if (stringField != null) {
            out.append(' ');
            out.append(stringField);
        }
        out.append("\n\n");
    }

    public void read(Scanner input) throws IOException {
        type = MsgType.valueOf( input.next() );
        id = input.nextInt();
        tickNumber = input.nextInt();

        for (int i = 0; input.hasNextInt(); i++) {
            fields[i] = input.nextInt();
        }

        if ( input.hasNextFloat() ) {
            x = input.nextFloat();
        }
        if ( input.hasNextFloat() ) {
            y = input.nextFloat();
        }
        String str = input.nextLine();

        if ( ! str.trim().isEmpty() ) {
            stringField = str;
        }
    }

    public int getId() {
        return id;
    }

    public MsgType getType() {
        return type;
    }

    public int getTickBegin() {
        return tickNumber;
    }

    public int getTickEnd() {
        return fields[0];
    }

    public boolean appliesToLocalPlayer() {
        return fields[0] != 0;
    }
    
    public void setAppliesToLocalPlayer(boolean applies) {
        fields[0] = (applies ? 1 : 0);
    }

    public int getColumn() {
        return fields[1];
    }

    public int getRow() {
        return fields[2];
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void get(Vector out) {
        out.set(x, y);
    }

    public String getTypeName() {
        return stringField;
    }

    public void newId() {
        id = nextId;
        ++nextId;
    }

    public void coin(int tickBegin, boolean thisPlayer, int col, int row) {
        newId();
        type = MsgType.COIN;
        tickNumber = tickBegin;
        fields[0] = (thisPlayer ? 1 : 0);
        fields[1] = col;
        fields[2] = row;
    }

    public void damaged(int tickBegin, float x, float y) {
        newId();
        type = MsgType.DAMAGED;
        tickNumber = tickBegin;
        this.x = x;
        this.y = y;
    }

    public void died(int tickBegin) {
        newId();
        type = MsgType.DIED;
        tickNumber = tickBegin;
    }

    public void enemySpawn(int tickBegin, String typename, float x, float y, int direction) {
        newId();
        type = MsgType.ENEMY_SPAWN;
        tickNumber = tickBegin;
        this.x = x;
        this.y = y;
        fields[0] = direction;
    }

    @Override
    public String toString() {
        return "EventMessage [id=" + id + ", type=" + type + ", tickNumber="
                + tickNumber + ", fields=" + Arrays.toString(fields) + ", x="
                + x + ", y=" + y + ", stringField=" + stringField + "]";
    }
    
}
