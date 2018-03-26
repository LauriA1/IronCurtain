package com.lamoid.ironcurtain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lamoid.ironcurtain.IronCurtain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = IronCurtain.WIDTH;
		config.height = IronCurtain.HEIGHT;
		config.title = IronCurtain.TITLE;
		new LwjglApplication(new IronCurtain(), config);
	}
}
