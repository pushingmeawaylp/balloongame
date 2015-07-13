package com.balloongame.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.balloongame.handlers.WidgetId;

/**
 * 
 * BaseClickListener can only be set listener for a Container
 *
 */
public class BaseClickListener extends ClickListener {
	
	public IClickListener listener;
	public ITouchListener touchListener;
	
	private boolean receiveParentEvents = true;
	
	public BaseClickListener(IClickListener listener){
		this.listener = listener;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		if(touchListener!=null) {
			touchListener.onTouchUp(event, x, y, pointer, button);
		}
		if(listener != null && !receiveParentEvents)
			event.stop();
		super.touchUp(event, x, y, pointer, button);
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		if(touchListener!=null) {
			touchListener.onTouchDown(event, x, y, pointer, button);
		}
		boolean touchDown = super.touchDown(event, x, y, pointer, button);
		if(listener != null && touchDown && !receiveParentEvents)
			event.stop();
		return touchDown; 
	}
	
	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		if(touchListener!=null) {
			touchListener.onTouchDragged(event, x, y, pointer);
		}
		super.touchDragged(event, x, y, pointer);
	}
	
	@Override
	public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
		super.enter(event, x, y, pointer, fromActor);
	}
	
	@Override
	public void clicked (InputEvent event, float x, float y) {
		if(listener != null){
			String name = event.getListenerActor().getName();
			if(name != null) {
				IWidgetId widgetId = WidgetId.getValue(name);
				widgetId.setActor(event.getListenerActor());
				widgetId.playSound();
				listener.click(widgetId);
			}
		}
		
		if(this.touchListener != null)
			touchListener.onTap(event, x, y, 0);
	}
	
	@Override
	public boolean handle(Event e) {
		if (!(e instanceof InputEvent)) return false;
		InputEvent event = (InputEvent)e;

		switch (event.getType()) {
			default:		
				return super.handle(e);
		}
	}
	
	public IClickListener getClickListener(){
		return listener;
	}
	
	public void setClickListener(IClickListener listener){
		this.listener = listener;
	}
	
	public ITouchListener getTouchListener() {
		return touchListener;
	}
	
	public void setTouchListener(ITouchListener listener) {
		this.touchListener=listener;
	}
	
	public void setReceiveParentEvents(boolean b) {
		receiveParentEvents= b;
	}
}

