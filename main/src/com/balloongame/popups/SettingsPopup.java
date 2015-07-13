package com.balloongame.popups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.balloongame.handlers.Container;
import com.balloongame.handlers.WidgetId;
import com.balloongame.listeners.IClickListener;
import com.balloongame.listeners.IWidgetId;
import com.balloongame.main.BalloonGame;
import com.balloongame.misc.Config.GameConfig;

public class SettingsPopup extends Container implements IClickListener
{
	Image bg, closeButtonImage_h, closeButtonImage_d, generic_button_image_h, generic_button_image_d;
	Container closeButtonContainer, mainContainer;
	Label titleLabel;	
	LabelStyle bigStyle, mediumStyle, smallStyle;
	Button closeButton; 
	
	static SettingsPopup settingsPopup = null;
	
	public SettingsPopup(WidgetId widgetId)
	{
		this.setName(widgetId.getName());
		this.setListener(this);

		initializeAssets();		
		initBgAndCloseButton();
		initTitle();
		initSettingsTiles();
		addScoreResetButton();
	}
	
	private void initializeAssets()
	{
		bg = new Image(new Texture(Gdx.files.internal("res/images/popupbg_mid.png")));
		closeButtonImage_h = new Image(new Texture(Gdx.files.internal("res/images/closeButton.png")));
		closeButtonImage_d = new Image(new Texture(Gdx.files.internal("res/images/closeButton.png")));
		generic_button_image_h = new Image(new Texture(Gdx.files.internal("res/images/button_generic_brown.png")));
		generic_button_image_d = new Image(new Texture(Gdx.files.internal("res/images/button_generic_brown_d.png")));
		
		bigStyle = new LabelStyle(BalloonGame.bigFont, Color.WHITE);
		mediumStyle = new LabelStyle(BalloonGame.mediumFont, Color.WHITE);
		smallStyle = new LabelStyle(BalloonGame.smallFont, Color.WHITE);

		mainContainer = new Container();
		mainContainer.setHeight(bg.getHeight());
		mainContainer.setWidth(bg.getWidth());
		mainContainer.setBackground(bg.getDrawable());		
		mainContainer.setX(-bg.getWidth()/2);
		mainContainer.setY(-bg.getHeight()/2);
		mainContainer.activate();
		
		this.addActor(mainContainer);
		
		closeButtonContainer = new Container(WidgetId.CLOSE_BUTTON);
		closeButtonContainer.setListener(this);
		closeButtonContainer.addListener(closeButtonContainer.getListener());
		closeButtonContainer.activate();
		
		registerButtons();
	}
	
	private void registerButtons()
	{
		closeButton = new Button(closeButtonImage_h.getDrawable(), closeButtonImage_d.getDrawable());		
		closeButton.setName(WidgetId.CLOSE_BUTTON.getName());
		closeButton.addListener(this.getListener());
		this.register(WidgetId.CLOSE_BUTTON, closeButton);
	}
	
	private void initBgAndCloseButton()
	{			
		closeButtonContainer.setHeight(closeButton.getHeight());
		closeButtonContainer.setWidth(closeButton.getWidth());
		
		closeButtonContainer.addActor(closeButton);
		closeButtonContainer.setX(mainContainer.getWidth() - closeButtonContainer.getWidth() - 8);
		closeButtonContainer.setY(mainContainer.getHeight() - closeButtonContainer.getHeight() - 8);
		
		mainContainer.addActor(closeButtonContainer);
	}
	
	private void initTitle()
	{
		titleLabel = new Label("Settings", bigStyle);
		mainContainer.addActor(titleLabel);
		titleLabel.setX(mainContainer.getWidth()/2 - titleLabel.getWidth()/2);
		titleLabel.setY(mainContainer.getHeight() - titleLabel.getHeight() - 3);
	}
	
	private void initSettingsTiles()
	{
		int tileNumber = 0;
		for(GameConfig config : GameConfig.values())
		{
			SettingsTile settingstile = new SettingsTile(config,mediumStyle, mediumStyle,
					WidgetId.getValue(config.name()), this);
			
			mainContainer.addActor(settingstile);	
			settingstile.setX(0);
			settingstile.setY(300 - 65*tileNumber++);				
		}
	}
	
	private void addScoreResetButton()
	{
		Button scoreResetButton =  new Button(generic_button_image_h.getDrawable(), generic_button_image_d.getDrawable());
		scoreResetButton.setName(WidgetId.SCORE_RESET_BUTTON.getName());
		scoreResetButton.addListener(this.getListener());
		this.register(WidgetId.SCORE_RESET_BUTTON, scoreResetButton);
		
		Label resetScoreLabel = new Label("Reset Score", smallStyle);
		
		scoreResetButton.addActor(resetScoreLabel);
		resetScoreLabel.setX(scoreResetButton.getWidth()/2 - resetScoreLabel.getWidth()/2);
		resetScoreLabel.setY(scoreResetButton.getHeight()/2 - resetScoreLabel.getHeight()/2);
		
		this.addActor(scoreResetButton);
		scoreResetButton.setX(this.getWidth()/2 - scoreResetButton.getWidth()/2);
		scoreResetButton.setY(this.getHeight() - 125);
	}
	
	@Override
	public void click(IWidgetId widgetId)
	{
		switch((WidgetId)widgetId)
		{
		case SCORE_RESET_BUTTON:
			BalloonGame.gameScore = 0;
			BalloonGame.gameScoreLabel.setText("" + BalloonGame.gameScore);
			break;
		case CLOSE_BUTTON:
			this.stash();
			break;
		default:
			break;
			
		}
	}

	@Override
	public void doubleClick(IWidgetId widgetId) {
		// TODO Auto-generated method stub
		
	}
	
	public static SettingsPopup getSettingsPopup()
	{
		if(settingsPopup == null)
			settingsPopup = new SettingsPopup(WidgetId.SETTINGS_POPUP);
		
		return settingsPopup;
	}
	
	private class SettingsTile extends Container implements IClickListener
	{
		Label nameLabel, difficultyLabel;
		Button difficultyButton;
		WidgetId widgetId;
		
		SettingsTile(GameConfig config, LabelStyle nameLabelStyle, LabelStyle diffcultyLabelStyle,
				WidgetId widgetId, IClickListener listener)
		{
			this.nameLabel = new Label(config.getName(), nameLabelStyle);
			this.difficultyLabel = new Label(config.getCurrentValue() + "", diffcultyLabelStyle);
			this.widgetId = widgetId;
			this.setListener(this);
			
			initSettingTile();
		}
		
		private void initSettingTile()
		{
			difficultyButton =  new Button(generic_button_image_h.getDrawable(), generic_button_image_d.getDrawable());
			difficultyButton.setName(widgetId.getName());
			difficultyButton.addListener(this.getListener());
			this.register(widgetId, difficultyButton);
			
			difficultyButton.addActor(difficultyLabel);
			difficultyLabel.setX(difficultyButton.getWidth()/2 - difficultyLabel.getWidth()/2);
			difficultyLabel.setY(difficultyButton.getHeight()/2 - difficultyLabel.getHeight()/2);
			
			this.addActor(difficultyButton);
			difficultyButton.setX(182);
			difficultyButton.setY(this.getHeight()/2 - difficultyButton.getHeight()/2);
			
			this.addActor(nameLabel);
			nameLabel.setX((bg.getWidth() - difficultyButton.getWidth())/2 - nameLabel.getWidth()/2);
			nameLabel.setY(this.getHeight()/2 - nameLabel.getHeight()/2);
		}
		
		@Override
		public void click(IWidgetId widgetId)
		{
			GameConfig config = GameConfig.getValue(widgetId.toString());
			float currentValue = config.getCurrentValue();
			float nextValue = 0;
			switch((WidgetId)widgetId)
			{
			case BALLOON_BASE_SPEED_X:
				nextValue = ++currentValue;
				break;
			case BALLOON_BASE_SPEED_Y:
			case BALLOON_FLOAT_TIME_DIVIDER:
			case BAR_BASE_SPEED_X:		
			case E:
			default:
				nextValue = currentValue + (config.getMaxValue() - config.getMinValue())/config.getNumSteps();
				break;		
			}
			
			if(nextValue < config.getMinValue())
				nextValue = config.getMaxValue();
			else if(nextValue > config.getMaxValue())
				nextValue = config.getMinValue();
			GameConfig.getValue(widgetId.toString()).setCurrentValue(nextValue);
			difficultyLabel.setText("" + nextValue);
		}
	}
}