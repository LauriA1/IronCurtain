package com.lamoid.ironcurtain;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lamoid.ironcurtain.state.GameStateManager;
import com.lamoid.ironcurtain.state.MenuState;

public class IronCurtain extends ApplicationAdapter {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;

	SpriteBatch batch;
	GameStateManager gsm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
