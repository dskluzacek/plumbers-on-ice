package com.plumbers.game.server;

import java.io.IOException;
import java.util.Scanner;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.plumbers.game.Vector;

public final class Message implements Poolable {
    private int id;
    private MsgType type;
    private int tickNumber;
    private int[] fields = new int[3];
    private float x, y;
    private String stringField;
    private static int nextId = 1;

    public enum MsgType {
        JUMP,
        JUMP_END,
        RUN_INPUT,
        RUN_INPUT_END,
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

    public boolean isForMyPlayer() {
        return fields[0] != 0;
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

    public void jump(int tickBegin) {
        newId();
        type = MsgType.JUMP;
        tickNumber = tickBegin;
    }

    public void jumpEnd(int tickEnd) {
        newId();
        type = MsgType.JUMP_END;
        tickNumber = tickEnd;
        fields[0] = tickEnd;
    }

    public void runInput(int tickBegin) {
        newId();
        type = MsgType.RUN_INPUT;
        tickNumber = tickBegin;
    }

    public void runInputEnd(int tickEnd) {
        newId();
        type = MsgType.RUN_INPUT_END;
        tickNumber = tickEnd;
        fields[0] = tickEnd;
    }

    public void coinCollect(int tickBegin, boolean thisPlayer, int col, int row) {
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
    
}
