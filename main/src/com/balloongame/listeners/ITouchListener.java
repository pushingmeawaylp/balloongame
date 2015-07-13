package com.balloongame.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*
 * Added in addition to TouchListener interface to distinguish its use for containers 
 */
public interface ITouchListener {

	public boolean onTouchDown(InputEvent event, float x, float y, int pointer, int button);

	public void onTouchDragged(InputEvent event, float x, float y, int pointer);

	public void onTouchUp (InputEvent event, float x, float y, int pointer, int button);

	public void onTap (InputEvent event, float x, float y, int count);
}

