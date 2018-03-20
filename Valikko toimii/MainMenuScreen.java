package com.lamoid.ironcurtain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen implements Screen {
    private final IronCurtain game;
    private OrthographicCamera camera;
    private ImageButton play_button, exit_button;
    Stage stage;

    private Texture play_texture, exit_texture;

    public MainMenuScreen(final IronCurtain gam) {
        game = gam;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        play_texture = new Texture( Gdx.files.internal("img/btn_play.png") );
        Drawable play_drawable = new TextureRegionDrawable(new TextureRegion(play_texture));
        play_button = new ImageButton(play_drawable);
        play_button.setPosition(screenWidth/2, screenHeight/2 + 30, Align.center);
        stage.addActor(play_button);

        play_button.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        game.setScreen(new GameScreen(game));
                        dispose();
                        return false;
                    }
                });

        exit_texture = new Texture( Gdx.files.internal("img/btn_exit.png") );
        Drawable exit_drawable = new TextureRegionDrawable(new TextureRegion(exit_texture));
        exit_button = new ImageButton(exit_drawable);
        exit_button.setPosition(screenWidth/2, screenHeight/2 - 30, Align.center);
        stage.addActor(exit_button);

        exit_button.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.exit();
                        return false;
                    }
                });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act();
        game.batch.begin();
        stage.draw();
/*
        game.font.draw(game.batch, "Welcome to IronCurtain! ",
                Gdx.graphics.getWidth()/2 - 300, Gdx.graphics.getHeight()/2 + 10);
        game.font.draw(game.batch, "Tap anywhere to begin!",
                Gdx.graphics.getWidth()/2 - 300, Gdx.graphics.getHeight()/2 - 10);
*/
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        play_texture.dispose();
        exit_texture.dispose();
    }
}