package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class IntroScreen implements Screen {

    private IronCurtain game;
    private Stage stage;

    public IntroScreen(IronCurtain gam){
        game = gam;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Texture logo_texture = new Texture ("logo.png");
        Image splashImage = new Image(logo_texture);
        splashImage.setSize(splashImage.getWidth()/splashImage.getHeight()*Gdx.graphics.getWidth()*0.6f,
                splashImage.getHeight()/splashImage.getWidth()*Gdx.graphics.getHeight()*0.6f);
        splashImage.setPosition((Gdx.graphics.getWidth() - splashImage.getWidth())/2,
                (Gdx.graphics.getHeight() - splashImage.getHeight())/2);

        splashImage.addAction(Actions.sequence(Actions.fadeIn(2f),
                Actions.fadeOut(2f), Actions.run(onSplashFinishedRunnable)));

        stage.addActor(splashImage);

        /*logo_sprite = new Sprite(logo_texture);
        logo_sprite.setSize(logo_sprite.getWidth()/logo_sprite.getHeight()*Gdx.graphics.getWidth()*0.6f,
                logo_sprite.getHeight()/logo_sprite.getWidth()*Gdx.graphics.getHeight()*0.6f);
        logo_sprite.setPosition((Gdx.graphics.getWidth() - logo_sprite.getWidth())/2,
                (Gdx.graphics.getHeight() - logo_sprite.getHeight())/2);*/
    }

    private Runnable onSplashFinishedRunnable = new Runnable() {
        @Override
        public void run() {
            game.changeScreen(IronCurtain.MAINMENU);
        }
    };

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
