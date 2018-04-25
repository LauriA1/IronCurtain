package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class MainMenuScreen implements Screen {
	private IronCurtain game;
	private Stage stage;

    private SpriteBatch batch;

	private Texture bg_texture, settings_texture, exit_texture;
	private Sprite bg_sprite;

    public MainMenuScreen(IronCurtain gam) {
		game = gam;
        batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
	}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        bg_texture = new Texture("menuBG.png");
        bg_sprite = new Sprite(bg_texture);
        bg_sprite.setSize(bg_sprite.getWidth() / bg_sprite.getHeight()
                * IronCurtain.screenHeight, IronCurtain.screenHeight);
        bg_sprite.setPosition((IronCurtain.screenWidth - bg_sprite.getWidth())/2,0);

        settings_texture = new Texture("menu_settings.png");
        Drawable settings_drawable = new TextureRegionDrawable(new TextureRegion(settings_texture));
        ImageButton settingsButton = new ImageButton(settings_drawable);
        settingsButton.setSize(IronCurtain.screenWidth / IronCurtain.screenHeight * settingsButton.getWidth() * 0.3f,
                IronCurtain.screenWidth / IronCurtain.screenHeight * settingsButton.getHeight() * 0.3f);
        settingsButton.setX(IronCurtain.screenWidth * 0.02f);
        settingsButton.setY(settingsButton.getX());
        settingsButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.changeScreen(IronCurtain.SETTINGS, 0);
                    }
                });
        stage.addActor(settingsButton);

        exit_texture = new Texture("menu_exit.png");
        Drawable exit_drawable = new TextureRegionDrawable(new TextureRegion(exit_texture));
        ImageButton exitButton = new ImageButton(exit_drawable);
        exitButton.setSize(exitButton.getWidth() / exitButton.getHeight() * settingsButton.getWidth(),
                settingsButton.getHeight());
        exitButton.setX(IronCurtain.screenWidth - IronCurtain.screenWidth * 0.08f);
        exitButton.setY(settingsButton.getX());
        exitButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Gdx.app.exit();
                    }
                });
        stage.addActor(exitButton);

        TextButton startButton = new TextButton(game.stringsBundle.get("start"), game.skin);
        startButton.setSize(startButton.getWidth() / startButton.getHeight() * settingsButton.getWidth() * 1.5f,
                settingsButton.getHeight() * 1.5f);
        startButton.setX(IronCurtain.screenWidth / 2 - startButton.getWidth() / 2);
        startButton.setY(IronCurtain.screenHeight * 0.1f);
        startButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.changeScreen(IronCurtain.THEGAME, 0);
                    }
                });
        stage.addActor(startButton);
    }

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        bg_sprite.draw(batch);
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
	    bg_texture.dispose();
        settings_texture.dispose();
        exit_texture.dispose();
	}
}
