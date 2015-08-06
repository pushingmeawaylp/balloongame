package com.balloongame.handlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.balloongame.listeners.BaseClickListener;
import com.balloongame.listeners.IClickListener;
import com.balloongame.listeners.IWidgetId;

public class Container extends Table implements IClickListener
{
	private BaseClickListener listener = null;
	protected IWidgetId widgetId;
	
	private Touchable forcedTouchable = null;
	
	public Container(){
	}

	public Container(IClickListener listener){
		this();
		this.setListener(listener);
	}

	public Container(String name){
		this();
		this.setName(name);
		this.setListener(this);
	}
	
	public Container(IWidgetId widgetId) {
		this(widgetId.getName());
		this.widgetId = widgetId;
	}
	
	/***************************
	 * 
	 * click listener
	 * 
	 ***************************/
	
	public void setListener(IClickListener listener) {
		if(this.listener == null){
			this.listener = this.getDefaultListener();
		}

		if(this.listener.getClickListener() != listener){
			this.removeListener();
			this.listener.setClickListener(listener);	
		}
	}
	
	public void removeListener() {
		if(this.getListener() == null) 
			return;
		this.getListener().setClickListener(null);

	}

	public BaseClickListener getListener() {
		return listener;
	}

	
	public BaseClickListener getDefaultListener() {
		return new BaseClickListener(null);
	}
	
	public void registerContainerListener(WidgetId widgetId, BaseClickListener listener)
	{
		this.setName(widgetId.getName());
		this.addListener(listener);
		this.register(widgetId, this);
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor actor = super.hit(x, y, touchable);
		if(actor != null){
			return actor;
		}
		return null;
	}
	
	public void register(IWidgetId widgetId, Actor actor) {
		if(widgetId != null)
			widgetId.setActor(actor);
	}

	@Override
	public void click(IWidgetId widgetId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doubleClick(IWidgetId widgetId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setTouchable(Touchable touchable) {
		if(this.forcedTouchable != null) return;
		
		super.setTouchable(touchable);
	}

	/**
	 * shows the element and makes it responsive to touch events.
	 */
	public boolean activate() {
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);
		return true;
	}

	/**
	 * hides the element and makes it non responsive to touch events.
	 */
	public void deactivate() {
		this.setVisible(false);
		this.setTouchable(Touchable.disabled);
	}
	
	public void stash()
	{
		this.deactivate();
	}

}
