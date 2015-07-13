package com.balloongame.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface IWidgetId {
	
	public void playSound();
	
	public String getName();
	
	public String getSuffix();
	
	public boolean equals(IWidgetId widgetId);
	
	public Actor getActor();
	
	public void setActor(Actor actor);
	
}
