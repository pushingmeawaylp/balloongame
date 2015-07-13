package com.balloongame.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.balloongame.handlers.WidgetId;
import com.balloongame.misc.Config;
import com.balloongame.popups.BalloonPopup;
import com.balloongame.popups.SettingsPopup;

public class GameStage extends Stage
{	
	/**
	 * Main group
	 */
	private Group mainGroup;
	
	BalloonPopup balloonPopup;
	
	private SettingsPopup settingsPopup;

	public GameStage(SpriteBatch spriteBatch) {
		super(Config.UI_VIEWPORT_WIDTH, Config.UI_VIEWPORT_HEIGHT, false, spriteBatch);
		
		mainGroup = new Group();
		mainGroup.setName("MAIN_GROUP");
		mainGroup.setTransform(false);
		mainGroup.setWidth(Config.UI_VIEWPORT_WIDTH);
		mainGroup.setHeight(Config.UI_VIEWPORT_HEIGHT);
		mainGroup.setVisible(true);
		addActor(mainGroup);
		
		Image bg = new Image(new Texture(Gdx.files.internal("res/images/main_bg.png")));
		this.mainGroup.addActor(bg);
		
		balloonPopup = new BalloonPopup(WidgetId.BALLOON_POPUP);
		this.mainGroup.addActor(balloonPopup);
	
		settingsPopup = SettingsPopup.getSettingsPopup();
		this.mainGroup.addActor(settingsPopup);
		settingsPopup.setX(this.getWidth()/2 - settingsPopup.getWidth()/2);
		settingsPopup.setY(this.getHeight()/2 - settingsPopup.getHeight()/2);
		settingsPopup.deactivate();
	}
	
	protected void drawMainGroup() {
		SpriteBatch batch = getSpriteBatch();
		getCamera().update();
		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		mainGroup.draw(batch, 1);
		batch.end();
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		balloonPopup.update(delta);
	}
	
	@Override
	public void draw()
	{
		drawMainGroup();
	}
	
	@Override
	public void dispose()
	{
		settingsPopup = null;
		super.dispose();
	}
	
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		return super.touchDown(x, y, pointer, button);
	}
	
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		return super.touchUp(x, y, pointer, button);
	}
	
	public SettingsPopup getSettingsPopup()
	{
		return settingsPopup;
	}
}
