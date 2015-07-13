package com.balloongame.listeners;

import com.badlogic.gdx.scenes.scene2d.Action;

public class ActionWithCompleteListener extends Action
{
	private ActionCompleteListener actionCompleteListener;
	
	public ActionWithCompleteListener initialize(ActionCompleteListener listener){
		this.actionCompleteListener = listener;
		return this;
	}
	
	@Override
	public boolean act(float delta) {
		this.actionCompleteListener.onActionCompleted(this);
		return true;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.actionCompleteListener = null;
	}
}
