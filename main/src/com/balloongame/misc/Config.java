package com.balloongame.misc;

import com.badlogic.gdx.Gdx;

public class Config
{
	public static String GAME_NAME = "Ballon Float";
	
	public static int viewportWidth = -1;//will be set on game start
	public static int viewportHeight = -1;//will be set on game start
	
	public static int VIEWPORT_DEFAULT_WIDTH = 480;
	public static int VIEWPORT_DEFAULT_HEIGHT = 800;
	public static int UI_VIEWPORT_WIDTH = VIEWPORT_DEFAULT_WIDTH;
	public static int UI_VIEWPORT_HEIGHT = VIEWPORT_DEFAULT_HEIGHT;
	public static int DESKTOP_VIEWPORT_WIDTH = (int)(VIEWPORT_DEFAULT_WIDTH * 0.9);
	public static int DESKTOP_VIEWPORT_HEIGHT = (int)(VIEWPORT_DEFAULT_HEIGHT * 0.9);
	
	public static int HUD_VIEWPORT_HEIGHT = 100;
	
	public static int MIN_ANGLE = 70;
	public static int MAX_ANGLE = 90;
	
	/**
	 * Scale factors for assets in case of multiple screens
	 */
	public static float scaleX = 1;
	public static float scaleY = 1;
	
	/**
	 * Delta time between consecutive frames in ms
	 */
	public static float deltaTime = 0f;
	
	public static int G = 10;       		// gravitational force
	
	public static int HOR_BAR_MOVING_TIME = 1;   // 1 sec
	
	public static float THRESHOLD_INDICATOR = 13f;
	
	public static int MIN_NUM_SAMPLES = 20;
	
	public static boolean TEST_MODE = false;
	
	public static void initialize()
	{
		viewportWidth = Gdx.graphics.getWidth();
		viewportHeight = Gdx.graphics.getHeight();

		VIEWPORT_DEFAULT_WIDTH = (int) scale(480);
		VIEWPORT_DEFAULT_HEIGHT = (int) scale(800);
		UI_VIEWPORT_WIDTH = VIEWPORT_DEFAULT_WIDTH;
		UI_VIEWPORT_HEIGHT = VIEWPORT_DEFAULT_HEIGHT;

		scaleX = (float) viewportWidth / VIEWPORT_DEFAULT_WIDTH;
		scaleY = (float) viewportHeight / VIEWPORT_DEFAULT_HEIGHT;
	}

	public static float scale(float num)
	{
		return scale(num, true);
	}

	public static float scale(float num, boolean shouldScale)
	{
		return num;
	}
	
	public enum GameConfig
	{
		BALLOON_BASE_SPEED_Y("Ball Speed Y :", 10, 10, 50),
//		BALLOON_BASE_SPEED_X("Ball Speed X :", 12, 6, 12),
//		BAR_BASE_SPEED_X("Bar Speed :", 0.6f, 0.1f, 0.6f),
		BALLOON_FLOAT_TIME_DIVIDER("Gravity :", 1, 1, 36),
		E("Bounce :" , 0.6f, 0.2f, 0.6f),
		;
		
		private String name;
		private float currentValue, minValue, maxValue;
		
		private int numSteps = 6;
		
		GameConfig(String name, float currentValue, float minValue, float maxValue)
		{
			this.setName(name);
			this.currentValue = currentValue;
			this.maxValue = maxValue;
			this.minValue = minValue;
		}
		
		public float getCurrentValue() {
			return currentValue;
		}

		public void setCurrentValue(float currentValue) {
			this.currentValue = currentValue;
		}

		public float getMinValue() {
			return minValue;
		}

		public float getMaxValue() {
			return maxValue;
		}

		public static GameConfig getValue(String configName) {
			try {
				return GameConfig.valueOf(Utility.toUpperCase(configName));
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
			}
			return GameConfig.E;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getNumSteps() {
			return numSteps;
		}
	}
	
	public enum HistoricalSamplingState
	{
		UNDER_VALUE(0),
		SAMPLING_STARTED(1),
		SAMPLING_ENDED(2),
		;
		
		private int value;
		
		HistoricalSamplingState(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}

}
