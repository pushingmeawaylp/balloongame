package com.balloongame.handlers;

import java.lang.ref.WeakReference;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.balloongame.listeners.IWidgetId;
import com.balloongame.misc.Utility;

public enum WidgetId implements IWidgetId
{	
	BALLOON,
	HORIZONTAL_BAR,
	BALLOON_POPUP,
	SETTINGS_POPUP,
	BLUETOOTH_POPUP,
	CLOSE_BUTTON,
	BALLOON_BASE_SPEED_Y,
	BALLOON_BASE_SPEED_X,
	BAR_BASE_SPEED_X,
	BALLOON_FLOAT_TIME_DIVIDER,
	E,
	SETTINGS_BUTTON,
	SCORE_RESET_BUTTON,
	COMMON,
    ;

	private String suffix;
	
	private String completeName;
	
	private WeakReference<Actor> actor;
	
	public static String SEPERATOR = ":";
	
	public static WidgetId getValue(String widgetName) {
		try {
			String[] values = widgetName.split(SEPERATOR);
			if(values.length > 1)
				return WidgetId.valueOf(Utility.toUpperCase(values[0])).setSuffix(values[1]);
			else
				return WidgetId.valueOf(Utility.toUpperCase(values[0]));
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
		}
		return WidgetId.COMMON;
	}
	
	public void setActor(Actor clickedActor){
		this.actor = new WeakReference<Actor>(clickedActor);
	}
	
	public Actor getActor(){
		if(actor != null) {
			return this.actor.get();
		}
		return null;
	}
	
	public static String getWidgetWithoutSuffix(WidgetId widgetId) {
		try {
			String[] values = widgetId.getName().split(SEPERATOR);
			return Utility.toUpperCase(values[0]);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
		}
		return WidgetId.COMMON.getName();
	}
	
	public String getName() {
		if(completeName == null)
			this.setName();
		return completeName;
	}
	
	public String getNameInUpperCase() {
		return Utility.toUpperCase(getName());
	}
	
	private void setName() {
		completeName = Utility.toLowerCase(this.name()) + (this.suffix == null ? "" : (SEPERATOR + this.suffix));
	}
	
	public String getSuffix() {
		return this.suffix;
	}
	
	public WidgetId setSuffix(String suffix) {
		this.suffix = suffix;
		this.setName();
		return this;
	}
	
	public static void disposeOnFinish(){
		for(WidgetId widgetId : values()){
			widgetId.setActor(null);
		}
	}
	
	@Override
	public boolean equals(IWidgetId widgetId) {
		return this.getName().equals(widgetId.getName());
	}

	public static WidgetId getWidgetId(String widgetIdString) {
		String[] parts = widgetIdString.split(WidgetId.SEPERATOR);
		WidgetId widgetId = WidgetId.valueOf(Utility.toUpperCase(parts[0]));
		if(parts.length>1)
			widgetId.setSuffix(parts[1]);
		return widgetId;
	}

	@Override
	public void playSound() {
		// TODO Auto-generated method stub
		
	}
}
