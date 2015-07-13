package com.balloongame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.balloongame.handlers.CustomSpriteBatch;
import com.balloongame.listeners.IClickListener;
import com.balloongame.listeners.ISensorUpdateListener;
import com.balloongame.listeners.IWidgetId;
import com.balloongame.misc.Config;

public class BalloonGame extends Game implements InputProcessor, IClickListener, ISensorUpdateListener
{	
	private SpriteBatch spriteBatch;
	
	public static GameStage gameStage;
	
	public static BitmapFont bigFont, mediumFont, smallFont, scoreFont;
	
	public static int gameScore = 0;
	
	public static Label gameScoreLabel;
	
	protected enum GameLoadState {
		PRE_INITIALIZE,
		GAME_INITIALIZED,
		CONTINUOUS_RENDER,
		PAUSED
	}
	
	protected static GameLoadState loadState = GameLoadState.PRE_INITIALIZE;
	
	public boolean isGameInitialized = false;
	
	@Override
	public void create()
	{	
		if(gameStage != null) {
			gameStage.dispose();
			gameStage.clear();
		}
		//Load Font
		bigFont = new BitmapFont(Gdx.files.internal("fonts/font_32.fnt"), false);
		mediumFont = new BitmapFont(Gdx.files.internal("fonts/font_18.fnt"), false);
		smallFont = new BitmapFont(Gdx.files.internal("fonts/font_14.fnt"), false);
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/font_50.fnt"), false);
		
		gameStage = new GameStage(getSpriteBatch());
		
		//set the game as the receiver for all the touch/mouse events.
		Gdx.input.setInputProcessor(this);
		
		//OPTIMIZATION : one time gl configs
		//orthographic projection, perspective correct texture coordinate interpolation not needed
		Gdx.gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		loadState = GameLoadState.GAME_INITIALIZED;
	}
		
	public void render()
	{
		alwaysRender();
		
		switch (loadState) {
		case GAME_INITIALIZED:
			loadPostInitialize();
			break;
		case CONTINUOUS_RENDER:
			renderGame();
			break;
		default:
			break;
		}
	}
	
	protected void loadPostInitialize() {
		if (!isGameInitialized) {
			//deviceApp.initializeGame();
			isGameInitialized = true;
		}		
		loadState = GameLoadState.CONTINUOUS_RENDER;
	}
	
	protected void renderGame()
	{
		gameStage.act(Gdx.graphics.getDeltaTime());
		gameStage.draw();
		return ;
	}

	protected void alwaysRender()
	{
		clearScreen();
		Config.deltaTime = Gdx.graphics.getDeltaTime();

		super.render();
	}
	
	private void clearScreen() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	private SpriteBatch getSpriteBatch() {
		if(this.spriteBatch == null) {
			this.spriteBatch = new CustomSpriteBatch();
		}
		
		return this.spriteBatch;
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
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return gameStage.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		return gameStage.touchUp(arg0, arg1, arg2,arg3);
	}

	@Override
	public void onSensorDataUpdate(double forceX, double forceY)
	{
		if(gameStage != null)
		{
			gameStage.balloonPopup.getBar().generateForce(forceX, false);
			gameStage.balloonPopup.getBalloon().generateForce(forceY, false);
		}
	}

	@Override
	public void onSensorStatusUpdate(int iStatusCode, String stDeviceName,
			String stNotificationTxt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() 
	{
		gameStage.dispose();
		super.dispose();
		
		gameScore = 0;
		gameScoreLabel = null;
	}
}