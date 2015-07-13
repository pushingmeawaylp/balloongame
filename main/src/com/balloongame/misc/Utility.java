package com.balloongame.misc;

import java.util.Locale;
import java.util.Random;

public class Utility
{
	public static String toUpperCase(String value) {
		return value == null ? null : toLowerCase(value).toUpperCase(Locale.ENGLISH);
	}
	
	public static String toLowerCase(String value) {
		return value == null ? null : value.toLowerCase(Locale.ENGLISH);
	}
	
	public enum Direction
	{ 
		E, W ;
		
		public static Direction getOppositeDirection(Direction direction)
		{
			switch(direction)
			{
			case E:
				return W;
			case W:
				return E;
			default:
				return E;
			}
		}
	};
	
	public static enum SPEEDS
	{
		SPEED_5(5),
		SPEED_10(10),
		SPEED_15(15),
		SPEED_20(20);
		
		int speed;
		SPEEDS(int speed)
		{
			this.speed = speed;
		}
	}
	
	public static int generateRandomNumber(int range)
	{
		Random random = new Random(range);
		return random.nextInt(range);
	}
	
	public static float generateRandomFloat()
	{
		Random random = new Random(10);
		return random.nextFloat();
	}
}
