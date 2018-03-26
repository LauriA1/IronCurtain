package com.lamoid.ironcurtain.state;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.sprites.Runner;

public class PlayState extends State implements ApplicationListener, InputProcessor {
    private Runner runner;
    private Texture playBackgroundFront;
    private Texture playBackground2;
    private Sprite playbgFront, playbgFront2;
    private Sprite playbg2;
    private Vector2 groundPos1, groundPos2;
    private Vector2 backgroundPos1, backgroundPos2;

    public PlayState(GameStateManager gsm){
        super(gsm);
        Gdx.input.setInputProcessor(this);
        playBackgroundFront = new Texture("ground_front.png");
        playBackground2 = new Texture("background.jpeg");
        playbgFront = new Sprite(playBackgroundFront);
        playbgFront2 = new Sprite(playBackgroundFront);
        playbg2 = new Sprite(playBackground2);

        cam.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        runner = new Runner(IronCurtain.HEIGHT / 2 + 50, IronCurtain.WIDTH / 2);

        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, 0);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + playbgFront.getWidth(), 0);

        backgroundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, 0);
        backgroundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + playbgFront.getWidth(), 0);

    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        runner.update(dt);
        updateGround();
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        cam.position.set(runner.getPosition().x , playbgFront.getY() + 130, 0);
        cam.update();
        sb.begin();
        //sb.draw(bgSprite, 0, 0,(bgSprite.getWidth() / bgSprite.getHeight() * Gdx.graphics.getHeight()), Gdx.graphics.getHeight())
        //sb.draw(playbg, 0, 40 - playbg.getHeight(),  (playbg.getWidth() / playbg.getHeight() * (Gdx.graphics.getHeight())), Gdx.graphics.getHeight());
        //sb.draw(playbgFront, 0, 0, playbgFront.getWidth() , (playbgFront.getHeight() / playbgFront.getWidth() * Gdx.graphics.getWidth()));
        //sb.draw(playbgFront2, 0, 0, playbgFront.getWidth() , (playbgFront.getHeight() / playbgFront.getWidth() * Gdx.graphics.getWidth()));
        sb.draw(playBackgroundFront, groundPos1.x, groundPos1.y, playbgFront.getWidth() , (playbgFront.getHeight() / playbgFront.getWidth() * Gdx.graphics.getWidth()));
        sb.draw(playBackgroundFront, groundPos2.x, groundPos2.y, playbgFront.getWidth() , (playbgFront.getHeight() / playbgFront.getWidth() * Gdx.graphics.getWidth()));
        //sb.draw(playbg2, IronCurtain.WIDTH , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(runner.getRunner(), runner.getPosition().x, runner.getPosition().y, (float) 30 , (runner.getHeight() / 10));
        sb.end();
    }

    private void updateGround(){
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + playbgFront.getWidth())
            groundPos1.add(playbgFront.getWidth() * 2, 0);
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + playbgFront.getWidth())
            groundPos2.add(playbgFront.getWidth() * 2, 0);
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
    public void dispose() {
        runner.dispose();
        playBackgroundFront.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP)
            runner.jump();
        if(keycode == Input.Keys.LEFT)
            runner.moveLeft();
        if(keycode == Input.Keys.RIGHT)
            runner.moveRight();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            runner.keyUpLeft();
        if(keycode == Input.Keys.RIGHT)
            runner.keyUpRight();
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
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
