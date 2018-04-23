package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class EndScreen implements Screen {
    private IronCurtain game;
    private Stage stage;
    private Texture texture;
    private Sprite background_sprite;
    private SpriteBatch batch;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font256;
    private BitmapFont font128;
    private GlyphLayout score_layout;
    private GlyphLayout tap_anywhere_layout;

    private int score;
    private String str_score;
    private String tap_anywhere;
    float font128X, font128Y, font256X, font256Y;
    private float deltaTime;

    public EndScreen(IronCurtain gam, float health){
        game = gam;
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());

        deltaTime = 3f;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("BebasNeue.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        font128 = generator.generateFont(parameter);
        parameter.size = 256;
        font256 = generator.generateFont(parameter);
        generator.dispose();

        tap_anywhere = "TAP ANYWHERE TO CONTINUE";
        tap_anywhere_layout = new GlyphLayout(font128, tap_anywhere);
        font128X = (IronCurtain.screenWidth - tap_anywhere_layout.width) / 2;
        font128Y = IronCurtain.screenHeight * -0.25f + (IronCurtain.screenHeight + tap_anywhere_layout.height) / 2;

        score = Math.round(health * 1000f);
        str_score = "YOU SCORED " + String.valueOf(score) + " PTS";
        score_layout = new GlyphLayout(font256, str_score);
        font256X = (IronCurtain.screenWidth - score_layout.width) / 2;
        font256Y = (IronCurtain.screenHeight + score_layout.height) / 2;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        texture = new Texture("end_screen.png");
        background_sprite = new Sprite(texture);
        background_sprite.setSize(background_sprite.getWidth() / background_sprite.getHeight()
                * IronCurtain.screenHeight, IronCurtain.screenHeight);
        background_sprite.setPosition((IronCurtain.screenWidth - background_sprite.getWidth())/2,0);

    }

    @Override
    public void render(float delta) {
        if ((deltaTime - Gdx.graphics.getDeltaTime() > 0)) {
            deltaTime -= Gdx.graphics.getDeltaTime();
        }
        else {
            deltaTime = 0;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background_sprite.draw(batch);
        if (deltaTime == 0) {
            font128.draw(batch, tap_anywhere_layout, font128X, font128Y);
        }

        font256.draw(batch, score_layout, font256X, font256Y);

        batch.end();

        stage.act(delta);
        stage.draw();

        if(Gdx.input.justTouched() && deltaTime == 0) {
            game.changeScreen(IronCurtain.MAINMENU, 0);
        }
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
