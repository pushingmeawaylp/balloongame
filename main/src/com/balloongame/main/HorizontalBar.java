package com.balloongame.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.balloongame.handlers.Container;
import com.balloongame.misc.Config;
import com.balloongame.misc.Config.GameConfig;
import com.balloongame.misc.Utility;
import com.balloongame.misc.Utility.Direction;

public class HorizontalBar extends Container
{
	Image barImg;
	
	private double coordX, coordY, currY, currX;
	
	private Direction direction;
	
	public boolean isMoving, forceApplied, isReset;
	
	public HorizontalBar()
	{
		barImg = new Image(new Texture(Gdx.files.internal("images/horizontal_bar.png")));
		this.setHeight(barImg.getHeight());
		this.setWidth(barImg.getWidth());
		this.addActor(barImg);
		
		reset();
	}
	
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
		
		if(force > 0)
			coordX -= 50;
		if(force < 0)
			coordX += 50;		
		
//		coordX -= (force * Config.BAR_BASE_SPEED_X)*getBarMovingTime();
		
		if(coordX <= 0)
			coordX = 0;
		else if(coordX >= Config.VIEWPORT_DEFAULT_WIDTH - this.getWidth())
			coordX = Config.VIEWPORT_DEFAULT_WIDTH - this.getWidth();
		
		forceApplied = true;
		isMoving = true;
	}
	
	public void changeParameters(float delta)
	{
		if(isMoving)
		{
			currY = getY();
			currX = getX();
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
		isMoving = false;
	}
	
	public float getBarMovingTime()
	{
		return 1;
//		return (float) (GameConfig.BAR_BASE_SPEED_X.getCurrentValue()
//				*Config.HOR_BAR_MOVING_TIME);
	}

	public Image getBarImg() {
		return barImg;
	}

	public void setBarImg(Image barImg) {
		this.barImg = barImg;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
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

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
}
