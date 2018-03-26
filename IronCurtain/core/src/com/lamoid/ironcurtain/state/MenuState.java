package com.lamoid.ironcurtain.state;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lamoid.ironcurtain.IronCurtain;

import java.applet.AppletContext;
import java.awt.*;

public class MenuState extends State implements ApplicationListener, InputProcessor{
    private Texture background;
    private Texture playButton;
    private Sprite spriteButton;
    private Sprite bgSprite;
    private Rectangle button;
    private Sprite buttonSprite;



    public MenuState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight() );
        Gdx.input.setInputProcessor(this);
        background = new Texture("background1.png");
        playButton = new Texture("btn_play.png");
        spriteButton = new Sprite(playButton);
        bgSprite = new Sprite(background);

        //button = new Rectangle(0, 0, (int) sprite.getWidth() , (int) sprite.getHeight());
        System.out.println("MenuState.java");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            System.out.println("Just Touch");
            gsm.set(new LevelState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //bgSprite.scale(Gdx.graphics.getBackBufferWidth() / bgSprite.getWidth());
        //bgSprite.setSize(bgSprite.getWidth() / bgSprite.getHeight() * Gdx.graphics.getHeight(), Gdx.graphics.getHeight());
        sb.draw(bgSprite, 0, 0,(bgSprite.getWidth() / bgSprite.getHeight() * Gdx.graphics.getHeight()), Gdx.graphics.getHeight());
        //spriteButton.setPosition(Gdx.graphics.getWidth() / 2 - spriteButton.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        sb.draw(spriteButton,(Gdx.graphics.getWidth() / 2 - spriteButton.getWidth() / 2), (Gdx.graphics.getHeight() / 2));
        spriteButton.setPosition(Gdx.graphics.getWidth() / 2 - spriteButton.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        //System.out.println(cam.position.x - sprite.getWidth() / 2);
        // sb.draw(sprite, cam.position.x - sprite.getWidth() / 2, cam.position.y);
        spriteButton.draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();

    }


    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
       /* System.out.println("Touch");
        System.out.println();
        System.out.println("screenX " + screenX);
        System.out.println("ScreenY " + screenY);
        System.out.println("sprite.getX " + spriteButton.getX());
        System.out.println("sprite.getY " + spriteButton.getY());
        System.out.println("sprite.getWidth() " + spriteButton.getWidth());
        System.out.println("sprite.getX() + sprite.getWidth() " + (spriteButton.getX() + spriteButton.getWidth()));
        if (screenX >= spriteButton.getX() && screenX <= (spriteButton.getX() + spriteButton.getWidth())){
            System.out.println("Press Button");
        }*/
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
