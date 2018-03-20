package com.lamoid.ironcurtain.util;

import com.lamoid.ironcurtain.screen.AbstractScreen;
import com.lamoid.ironcurtain.screen.GameScreen;
import com.lamoid.ironcurtain.screen.LevelSelectScreen;
import com.lamoid.ironcurtain.screen.MainMenuScreen;

public enum ScreenEnum {
	
	MAIN_MENU {
		public AbstractScreen getScreen(Object... params) {
			return new MainMenuScreen();
		}
	},
	
	LEVEL_SELECT {
		public AbstractScreen getScreen(Object... params) {
			return new LevelSelectScreen();
		}
	},
	
	GAME {
		public AbstractScreen getScreen(Object... params) {
			return new GameScreen((Integer) params[0]);
		}
	};
	
	public abstract AbstractScreen getScreen(Object... params);
}