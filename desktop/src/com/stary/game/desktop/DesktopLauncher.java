package com.stary.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.stary.game.StaryGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Stary";
		config.width =1080;
//		config.allowSoftwareMode=true;
		config.height = 608;//.5
		new LwjglApplication(new StaryGame(), config);
	}
}
