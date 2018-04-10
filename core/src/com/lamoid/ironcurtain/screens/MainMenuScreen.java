package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class MainMenuScreen implements Screen {
	private IronCurtain game;
	private Stage stage;

    private SpriteBatch batch;

	private Texture texture;
	private Sprite background_sprite;

	public MainMenuScreen(IronCurtain gam) {
		game = gam;
        batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
	}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        texture = new Texture("background1.png");
        background_sprite = new Sprite(texture);
        background_sprite.setSize(background_sprite.getWidth()/background_sprite.getHeight()*Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background_sprite.setPosition((Gdx.graphics.getWidth() - background_sprite.getWidth())/2,0);

        TextButton play_button = new TextButton(game.stringsBundle.get("play"), game.skin, "custom");
        TextButton settings_button = new TextButton(game.stringsBundle.get("settings"), game.skin, "custom");
        TextButton exit_button = new TextButton(game.stringsBundle.get("exit"), game.skin, "custom");

        table.add(play_button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(settings_button).fillX().uniformX();
        table.row();
        table.add(exit_button).fillX().uniformX();

        play_button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.changeScreen(IronCurtain.THEGAME);
                    }
                });

        settings_button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.changeScreen(IronCurtain.SETTINGS);
                    }
                });

        exit_button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Gdx.app.exit();
                    }
                });
    }

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background_sprite.draw(batch);
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
        this.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
        batch.dispose();
	    stage.dispose();
	    texture.dispose();
	}
}
