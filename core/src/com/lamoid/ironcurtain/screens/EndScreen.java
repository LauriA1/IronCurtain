package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class EndScreen implements Screen {
    private IronCurtain game;
    private Stage stage;
    private Texture texture;
    private Sprite background_sprite;
    private SpriteBatch batch;
    private BitmapFont font;

    private float score;

    public EndScreen(IronCurtain gam, float health){
        game = gam;
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();

        score = health * 1000f;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        texture = new Texture("background1.png");
        background_sprite = new Sprite(texture);
        background_sprite.setSize(background_sprite.getWidth() / background_sprite.getHeight()
                * IronCurtain.screenHeight, IronCurtain.screenHeight);
        background_sprite.setPosition((IronCurtain.screenWidth - background_sprite.getWidth())/2,0);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background_sprite.draw(batch);
        font.getData().setScale(10f);
        font.draw(batch, String.format("%.0f", Math.round(score)), IronCurtain.screenWidth / 2, IronCurtain.screenHeight / 2);

        batch.end();

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
        batch.dispose();
        stage.dispose();
        texture.dispose();
    }
}
