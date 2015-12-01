package com.plumbers.game.server;

import java.util.Scanner;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.Character;
import com.plumbers.game.Motionable;
import com.plumbers.game.Vector;

public final class StateMessage implements Message, Poolable {
    private float xPos, yPos;
    private float xVel, yVel;
    private float xAccel, yAccel;
    private Character.State state;
    private int tickNumber;
    
    private StateMessage() {}
    
    public static StateMessage obtain() {
        return Pools.obtain(StateMessage.class);
    }
    
    @Override
    public void reset() {
        xPos = Float.NaN;
        yPos = Float.NaN;
        xVel = Float.NaN;
        yVel = Float.NaN;
        xAccel = Float.NaN;
        yAccel = Float.NaN;
        state = null;
        tickNumber = 0;
    }
    
    @Override
    public void write(StringBuilder out)
    {
        out.setLength(0);
        out.append("STATE ").append(tickNumber).append(' ');
        out.append( state.name() );
        out.append('\n');
        out.append(xPos).append(' ');
        out.append(yPos).append(' ');
        out.append(xVel).append(' ');
        out.append(yVel).append(' ');
        out.append(xAccel).append(' ');
        out.append(yAccel);
        out.append("\n\n");
    }
    
    public void read(Scanner scanner) {
        scanner.next();
        tickNumber = scanner.nextInt();
        state = Character.State.valueOf( scanner.next() );
        xPos = scanner.nextFloat();
        yPos = scanner.nextFloat();
        xVel = scanner.nextFloat();
        yVel = scanner.nextFloat();
        xAccel = scanner.nextFloat();
        yAccel = scanner.nextFloat();
    }
    
    public StateMessage setValues(Character character, int tickNumber)
    {
        this.tickNumber = tickNumber;
        state = character.getState();
        xPos = character.getXPosition();
        yPos = character.getYPosition();
        xVel = character.getXVelocity();
        yVel = character.getYVelocity();
        xAccel = character.getXAcceleration();
        yAccel = character.getYAcceleration();

        return this;
    }
    
    public void updatePosition(Motionable out) {
        out.setPosition(xPos, yPos);
    }

    public void updateVelocity(Motionable out) {
        out.setVelocity(xVel, yVel);
    }
    
    public void updateAcceleration(Motionable out) {
        out.setAcceleration(xAccel, yAccel);
    }
    
    public void getPosition(Vector out) {
        out.set(xPos, yPos);
    }

    public void getVelocity(Vector out) {
        out.set(xVel, yVel);
    }
    
    public void getAcceleration(Vector out) {
        out.set(xAccel, yAccel);
    }

    public Character.State getState() {
        return state;
    }

    public int getTickNumber() {
        return tickNumber;
    }

}
