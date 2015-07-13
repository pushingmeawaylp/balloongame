package com.balloongame.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.balloongame.handlers.Container;
import com.balloongame.misc.Config;
import com.balloongame.misc.Config.GameConfig;
import com.balloongame.misc.Utility;
import com.balloongame.misc.Utility.Direction;
import com.balloongame.popups.BalloonPopup;


public class Balloon extends Container
{
	private double coordX, coordY, currY, currX;

	private double timeElapsed, startVelocityY, startVelocityX, 
			currentVelocityY, currentVelocityX, timeOfFloat, tempTimeOfFloat,
			angle, maxReachedHeight;
	
	private double tempY, tempX;

	Image balloonImg;
	
	public boolean isMoving, forceApplied, toHitWall, isReset;
	
	private Direction direction;
	
	public int bounceCounter = 0;
	
	int currentAnimationCount = 1;
	float animationTimeDuration = 0.06f; //msec
	int numOfAnimationFrames = 7;
	
	public boolean isAnimationRunning = false;
	
	private BalloonPopup balloonPopup;

	public Balloon(BalloonPopup popup)
	{
		balloonImg = new Image(new Texture(Gdx.files.internal("res/images/animation/balloon" +
				currentAnimationCount + ".png")));
		this.setHeight(balloonImg.getHeight());
		this.setWidth(balloonImg.getWidth());
		this.addActor(balloonImg);
		
		reset();
		setDirection(Direction.E);
		
		balloonPopup = popup;
	}
	
	/**
	 *  x = time
	 *  y = EMG Value varying between +2 and -2
	 */
	public void generateForce(double force, boolean isRandomForce)
	{	
		if(force == 0 && !isRandomForce)
			return;
		
		if(isRandomForce)
		{
			int integer = Utility.generateRandomNumber(10);
			float fl = Utility.generateRandomFloat();
		
			force = integer + fl;
		}
		
		angle = 90;
//		while(angle < Config.MIN_ANGLE)
//			angle = Utility.generateRandomNumber(Config.MAX_ANGLE);
		
		angle = Math.toRadians(angle);
		double forceX = force*(Math.cos(angle));
		double forceY = force*(Math.sin(angle));
		
		/**
		 * for Y movement
		 */
		startVelocityY = (forceY * GameConfig.BALLOON_BASE_SPEED_Y.getCurrentValue()) + currentVelocityY;
		if(startVelocityY <= 0)
		{
			startVelocityY = (forceY * GameConfig.BALLOON_BASE_SPEED_Y.getCurrentValue());
		}
		currentVelocityY = startVelocityY;
		
		double toMoveY = currY + ((startVelocityY * startVelocityY)/ (2*Config.G));
		coordY = toMoveY > (Config.UI_VIEWPORT_HEIGHT - getHeight()) ? 
				(Config.UI_VIEWPORT_HEIGHT - getHeight()) : toMoveY;
		timeElapsed = 0;
		
		if(coordY == toMoveY)
			timeOfFloat = Math.sqrt(((2*(coordY-currY))/(Config.G*
						GameConfig.BALLOON_FLOAT_TIME_DIVIDER.getCurrentValue())));
		else
		{
			float finalVelocity = (float) Math.sqrt((startVelocityY*startVelocityY) - 2*Config.G*(coordY-currY));
			timeOfFloat = (startVelocityY - finalVelocity)/(2*Config.G);
		}
		
//		/**
//		 *  for X movement
//		 */
//		startVelocityX = (forceX * GameConfig.BALLOON_BASE_SPEED_X.getCurrentValue()) + currentVelocityX;
//		if(startVelocityX <= 0)
//		{
//			startVelocityX = (forceX * GameConfig.BALLOON_BASE_SPEED_X.getCurrentValue());
//		}
//		currentVelocityX = startVelocityX;
//		
//		if(direction == Direction.E)
//		{
//			double toMoveX = currX + startVelocityX*timeOfFloat;
//			if(toMoveX > (Config.UI_VIEWPORT_WIDTH - getWidth()))
//			{
//				tempTimeOfFloat = (Config.UI_VIEWPORT_WIDTH - getWidth() - currX)/
//						(GameConfig.BALLOON_FLOAT_TIME_DIVIDER.getCurrentValue()*currentVelocityX);
//				coordX = Config.UI_VIEWPORT_WIDTH - getWidth() - (timeOfFloat - tempTimeOfFloat)*currentVelocityX;
//				if(coordX < 0)
//					coordX = 0;
//				if(coordX > Config.VIEWPORT_DEFAULT_WIDTH - getWidth())
//					coordX = Config.VIEWPORT_DEFAULT_WIDTH - getWidth();
//				tempY = currY + startVelocityY*tempTimeOfFloat - (0.5*Config.G*tempTimeOfFloat*tempTimeOfFloat);
//				if(tempY > Config.VIEWPORT_DEFAULT_HEIGHT - getHeight())
//					tempY = Config.VIEWPORT_DEFAULT_HEIGHT - getHeight();
//				toHitWall = true;
//			}
//			else
//				coordX = toMoveX;
//		}
//		else
//		{
//			double toMoveX = currX - startVelocityX*timeOfFloat;
//			if(toMoveX < 0)
//			{
//				tempTimeOfFloat = currX/currentVelocityX;
//				coordX = (timeOfFloat - tempTimeOfFloat)*currentVelocityX;
//				if(coordX < 0)
//					coordX = 0;
//				if(coordX > Config.VIEWPORT_DEFAULT_WIDTH - getWidth())
//					coordX = Config.VIEWPORT_DEFAULT_WIDTH - getWidth();
//				tempY = currY + startVelocityY*tempTimeOfFloat - (0.5*Config.G*tempTimeOfFloat*tempTimeOfFloat);
//				if(tempY > Config.VIEWPORT_DEFAULT_HEIGHT - getHeight())
//					tempY = Config.VIEWPORT_DEFAULT_HEIGHT - getHeight();
//				toHitWall = true;
//			}
//			else
//				coordX = toMoveX;
//		}
		
		forceApplied = true;
		isMoving = true;
	}
	
	public void changeParameters(float delta)
	{
		if(isMoving)
		{
			timeElapsed += delta;
			currentVelocityY = startVelocityY - Config.G*timeElapsed;
			currY = getY();
			currX = getX();
			
			if(currY >= Config.UI_VIEWPORT_HEIGHT - getHeight())
			{
				isAnimationRunning = true;		
			}
		}
		else
		{
			reset();
		}
	}
	
	public void reset()
	{	
		coordX = currX = (int) (Config.UI_VIEWPORT_WIDTH/2 - this.getWidth()/2);
		coordY = currY = 0;
		
		startVelocityY = startVelocityX = currentVelocityY = currentVelocityX = timeOfFloat = timeElapsed = 0f;
		
		isMoving = false;
		toHitWall = false;
		forceApplied = false;
		bounceCounter = 0;
		currentAnimationCount = 1;
		setDirection(Direction.E);
		
		this.removeActor(balloonImg);
		
		balloonImg = new Image(new Texture(Gdx.files.internal("res/images/animation/balloon" +
				currentAnimationCount + ".png")));
		addActor(balloonImg);
		isAnimationRunning = false;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta)
	{
		if(isAnimationRunning)
		{	
			animationTimeDuration -= delta;
			
			if(animationTimeDuration <= 0)
			{	
				this.removeActor(balloonImg);
				balloonImg = new Image(new Texture(Gdx.files.internal("res/images/animation/balloon" +
						currentAnimationCount++ + ".png")));

				this.addActor(balloonImg);
				if(currentAnimationCount == 8)
				{
					isAnimationRunning = false;
					currentAnimationCount = 1;
					balloonPopup.resetPopup();
				}
				animationTimeDuration = 0.06f;
			}
		}
		super.act(delta);
	}
		
	public double getTimeElapsed() {
		return timeElapsed;
	}
	
	public double getTimeOfFloat() {
		return timeOfFloat;
	}
	
	public double getCoordX() {
		return coordX;
	}

	public double getCoordY() {
		return coordY;
	}
	
	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	public double getCurrY() {
		return currY;
	}

	public void setCurrY(double currY) {
		this.currY = currY;
	}

	public double getCurrX() {
		return currX;
	}

	public void setCurrX(double currX) {
		this.currX = currX;
	}
	
	public double getTempTimeOfFloat() {
		return tempTimeOfFloat;
	}
	
	public double getTempY() {
		return tempY;
	}
	
	public double getTempX() {
		return tempX;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public double getMaxReachedHeight() {
		return maxReachedHeight;
	}

	public void setMaxReachedHeight(double maxReachedHeight) {
		this.maxReachedHeight = maxReachedHeight;
	}
	
	public void setTimeOfFloat(double timeOfFloat) {
		this.timeOfFloat = timeOfFloat;
	}
}
