package com.balloongame.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.balloongame.misc.Config;

public class BalloonGameDesktop
{	
	static BalloonGame game = null;
	
	public static void main (String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.useGL20 = true;
		cfg.r = 8;
		cfg.g = 8;
		cfg.b = 8;
		cfg.a = 8;
		cfg.depth = 16;
		cfg.title = Config.GAME_NAME;
		cfg.width = (int) (Config.UI_VIEWPORT_WIDTH * Config.scaleX);
		cfg.height = (int) (Config.UI_VIEWPORT_HEIGHT * Config.scaleY);
		
		game = new BalloonGame();
		new LwjglApplication(game, cfg);
	}
}

