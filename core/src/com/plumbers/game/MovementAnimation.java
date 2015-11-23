package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class MovementAnimation {
	private final Animation idleAnim;
	private final Animation runAnim;
	private final Animation jumpAnim;
	private final Animation landAnim;
	private final Animation knockbackAnim;
	
	private Action action = Action.IDLE;
	
	public MovementAnimation(Animation idleAnim, Animation runAnim,
	    Animation jumpAnim, Animation landAnim, Animation knockbackAnim)
	{
		this.idleAnim = idleAnim;
		this.runAnim = runAnim;
		this.jumpAnim = jumpAnim;
		this.landAnim = landAnim;
		this.knockbackAnim = knockbackAnim;
	}

	public void setAction(Action action) {
		if (action == null)
			throw new IllegalArgumentException();
		
		this.action = action;
	}

	public TextureRegion getFrame(float currentTime) {
		switch (action) {
		case IDLE:
			return idleAnim.getKeyFrame(currentTime, true);
		case JUMP:
			return jumpAnim.getKeyFrame(currentTime, true);
		case LAND:
			return landAnim.getKeyFrame(currentTime, true);
		case KNOCKED_BACK:
			return knockbackAnim.getKeyFrame(currentTime, true);
		case RUN:
			return runAnim.getKeyFrame(currentTime, true);
		}
		return null;
	}

	public enum Action {
		IDLE,
		RUN,
		JUMP,
		LAND,
		KNOCKED_BACK;
	}
}
