package com.lamoid.ironcurtain;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lamoid.ironcurtain.state.GameStateManager;
import com.lamoid.ironcurtain.state.MenuState;

public class IronCurtain extends ApplicationAdapter {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Iron Curtain";
	private SpriteBatch batch;
	private GameStateManager gsm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Gdx.gl.glClearColor(0, 0, 1, 1);
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		super.dispose();
	}


}
