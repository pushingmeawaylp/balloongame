package com.balloongame.popups;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.balloongame.handlers.Container;
import com.balloongame.handlers.WidgetId;
import com.balloongame.listeners.ActionCompleteListener;
import com.balloongame.listeners.ActionWithCompleteListener;
import com.balloongame.listeners.IClickListener;
import com.balloongame.listeners.IWidgetId;
import com.balloongame.main.Balloon;
import com.balloongame.main.BalloonGame;
import com.balloongame.main.HorizontalBar;
import com.balloongame.misc.Config;
import com.balloongame.misc.Config.GameConfig;
import com.balloongame.misc.Utility.Direction;

public class BalloonPopup extends Container  implements ActionCompleteListener, IClickListener
{
	private Balloon balloon;
	private HorizontalBar bar;
	EventListener listener;
	
	Button settingsButton;
	Image settingsButtonImage_h, settingsButtonImage_d;
	
	public BalloonPopup(WidgetId widgetId)
	{	
		bar = new HorizontalBar();
		bar.setY(0);
		bar.setX(Config.UI_VIEWPORT_WIDTH/2 - bar.getWidth()/2);		
		this.addActor(bar);	
		
		balloon = new Balloon(this);
		balloon.setY(bar.getHeight());
		balloon.setX(Config.UI_VIEWPORT_WIDTH/2 - balloon.getWidth()/2);
		this.addActor(balloon);	
		
		this.setName(widgetId.getName());
		this.setListener(this);
	
		balloon.registerContainerListener(WidgetId.BALLOON, this.getListener());
		bar.registerContainerListener(WidgetId.HORIZONTAL_BAR, this.getListener());
		
		addSettingsButton();
		
		addWincOunterLabel();
	}
	
	private void addSettingsButton()
	{
		settingsButtonImage_h = new Image(new Texture(Gdx.files.internal("res/images/settingsButton.png")));
		settingsButtonImage_d = new Image(new Texture(Gdx.files.internal("res/images/settingsButton.png")));
		
		settingsButton = new Button(settingsButtonImage_h.getDrawable(), 
									settingsButtonImage_d.getDrawable());
		
		this.addActor(settingsButton);
		settingsButton.setX(Config.UI_VIEWPORT_WIDTH - settingsButton.getWidth() - 10);
		settingsButton.setY(Config.UI_VIEWPORT_HEIGHT - settingsButton.getHeight() - 10);
		
		settingsButton.setName(WidgetId.SETTINGS_BUTTON.getName());
		settingsButton.addListener(this.getListener());
		this.register(WidgetId.SETTINGS_BUTTON, settingsButton);
	}
	
	private void addWincOunterLabel()
	{
		BalloonGame.gameScoreLabel = new Label("" + BalloonGame.gameScore, new LabelStyle(BalloonGame.scoreFont, Color.BLACK));
		this.addActor(BalloonGame.gameScoreLabel);
		BalloonGame.gameScoreLabel.setX(BalloonGame.gameScoreLabel.getWidth() - 5);
		BalloonGame.gameScoreLabel.setY(Config.UI_VIEWPORT_HEIGHT - BalloonGame.gameScoreLabel.getHeight() - 10);
	}
	
	public void update(float delta)
	{
		moveBalloon(delta);
		moveBar(delta);
	}
	
	private void moveBalloon(float delta)
	{
		Action sequenceAction = null;
		Action actionUpwards = null;
		balloon.changeParameters(delta);
		if(balloon.forceApplied)
		{
			balloon.bounceCounter = 0;
			balloon.clearActions();
			
			if(!balloon.toHitWall)
			{
				actionUpwards = Actions.moveTo((float)balloon.getCoordX(), 
					(float)balloon.getCoordY(), 
					(float)balloon.getTimeOfFloat(),
					Interpolation.pow2Out);
			}
			else
			{
				actionUpwards = Actions.sequence(
								Actions.moveTo(
									(balloon.getDirection() == Direction.E) ?
											Config.VIEWPORT_DEFAULT_WIDTH - balloon.getWidth() : 0, 
											(float)balloon.getTempY(), 
											(float)balloon.getTempTimeOfFloat(),
											Interpolation.linear),
								new Action()
								{		
									@Override
									public boolean act(float delta) {
										balloon.setDirection(Direction.getOppositeDirection(balloon.getDirection()));
										return true;
									}
					
								},
								Actions.moveTo((float)balloon.getCoordX(), 
									(float)balloon.getCoordY(), 
									(float)(balloon.getTimeOfFloat()- balloon.getTempTimeOfFloat()),
									Interpolation.pow2Out)
				);
				balloon.toHitWall = false;
			}
			sequenceAction = sequence(actionUpwards,
					Actions.action(ActionWithCompleteListener.class).initialize(this));
			balloon.addAction(sequenceAction);
			balloon.forceApplied = false;
			balloon.isMoving = true;
		}
		else if(balloon.isReset)
		{
			actionUpwards = Actions.moveTo(Config.VIEWPORT_DEFAULT_WIDTH/2 - balloon.getWidth()/2, 
					bar.getHeight(), 
					(float) 0.01);
			balloon.addAction(actionUpwards);
			balloon.reset();
			balloon.isReset = false;
		}
	}
	
	public void moveBar(float delta)
	{
		Action actionSidewards = null;
		bar.changeParameters(delta);
		if(bar.forceApplied)
		{	
			bar.clearActions();
			actionSidewards = Actions.moveTo((float)bar.getCoordX(), 
					(float)bar.getCoordY(), 
					(float)bar.getBarMovingTime(),
					Interpolation.linear);
			bar.addAction(actionSidewards);
			bar.forceApplied = false;
			bar.isMoving = true;
		}
		else if(bar.isReset)
		{
			bar.clearActions();
			actionSidewards = Actions.moveTo(Config.VIEWPORT_DEFAULT_WIDTH/2 - bar.getWidth()/2, 
					0, (float) 0.01);
			bar.addAction(actionSidewards);
			bar.reset();
			bar.isReset = false;
		}
	}
	
	private void addBounceAction()
	{
		//bounce back
		if((balloon.getX() + balloon.getWidth()/2 >= bar.getX()) && 
				(balloon.getX() - balloon.getWidth()/2 <= (bar.getX() + bar.getWidth())))
		{
			if(balloon.getCoordY() <= bar.getHeight())
			{
				balloon.forceApplied = false;
				return;
			}
			balloon.forceApplied = true;
			balloon.toHitWall = false;
			balloon.setCoordY(balloon.getCoordY()*GameConfig.E.getCurrentValue() <= bar.getHeight() ?
					bar.getHeight() : balloon.getCoordY()*GameConfig.E.getCurrentValue());
			
			float timeOfFloat = (float)Math.sqrt(
					(2*(balloon.getMaxReachedHeight()*(Math.pow(GameConfig.E.getCurrentValue(),
							++balloon.bounceCounter)))
					)/(Config.G * GameConfig.BALLOON_FLOAT_TIME_DIVIDER.getCurrentValue()));
			balloon.setTimeOfFloat(timeOfFloat);
		}
		else
		{
			balloon.isReset = true;
			bar.isReset = true;
			BalloonGame.gameScore--;
			updateGameScore();
		}
	}
	
	@Override
	public void onActionCompleted(Action action)
	{
		if(balloon.isMoving)
		{
			balloon.setMaxReachedHeight(balloon.getY());
			//downward decent
			float time = (float) Math.sqrt(((2*balloon.getY())/(Config.G * 
					GameConfig.BALLOON_FLOAT_TIME_DIVIDER.getCurrentValue())));
			Action actionDownwards = Actions.sequence(
					Actions.moveTo(balloon.getX(), bar.getHeight(), time, Interpolation.pow2In),
					new Action()
					{
						@Override
						public boolean act(float arg0) {
							addBounceAction();
							return true;
						}
					}
			);
			balloon.addAction(actionDownwards);
		}
	}

	@Override
	public void click(IWidgetId widgetId)
	{
		switch((WidgetId)widgetId)
		{
		case BALLOON:
			//balloon.isAnimationRunning = true;
			balloon.generateForce(0, true);
			break;
		case HORIZONTAL_BAR:
			bar.generateForce(0, true);
			break;
		case SETTINGS_BUTTON:
			BalloonGame.gameStage.getSettingsPopup().activate();
			break;
		default:
			break;
		}
	}
	
	public void resetPopup()
	{
		balloon.clearActions();
		balloon.isReset = true;	
		bar.clearActions();
		bar.isReset = true;	
		
		BalloonGame.gameScore++;
		updateGameScore();
	}
	
	public void updateGameScore()
	{
		BalloonGame.gameScoreLabel.setText("" + BalloonGame.gameScore);
	}

	@Override
	public void doubleClick(IWidgetId widgetId) {
		// TODO Auto-generated method stub
		
	}
	
	public Balloon getBalloon() {
		return balloon;
	}

	public HorizontalBar getBar() {
		return bar;
	}

}
