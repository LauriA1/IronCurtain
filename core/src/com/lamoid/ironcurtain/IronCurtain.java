package com.lamoid.ironcurtain;

import com.badlogic.gdx.Game;
import com.lamoid.ironcurtain.util.ScreenEnum;
import com.lamoid.ironcurtain.util.ScreenManager;

public class IronCurtain extends Game {

	@Override
	public void create () {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen( ScreenEnum.MAIN_MENU );
	}
}
