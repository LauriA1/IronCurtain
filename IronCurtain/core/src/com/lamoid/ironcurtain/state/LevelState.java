package com.lamoid.ironcurtain.state;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import javafx.scene.shape.Rectangle;

public class LevelState extends State implements ApplicationListener, InputProcessor {

    ShapeRenderer shapeRenderer;
    Rectangle leftBtn, rightBtn;

    private float screenWidth, screenHeight;

    public LevelState(GameStateManager gsm) {
        super(gsm);
        Gdx.input.setInputProcessor(this);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        System.out.println(screenWidth + " : " + screenHeight);

        leftBtn = new Rectangle();
        leftBtn.setX(screenWidth / 2 - 200);
        leftBtn.setY(screenHeight / 2);
        leftBtn.setWidth(400);
        leftBtn.setHeight(screenHeight / 15);

        rightBtn = new Rectangle();
        rightBtn.setX(screenWidth / 2 - 200);
        rightBtn.setY(screenHeight / 2 + (screenHeight / 15) * 2);
        rightBtn.setWidth(400);
        rightBtn.setHeight(screenHeight / 15);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void create() {

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(80 / 255.0f, 80 / 255.0f, 50 / 255.0f, 1);
        shapeRenderer.rect((float) leftBtn.getX(), (float) leftBtn.getY(), (float) leftBtn.getWidth(), (float) leftBtn.getHeight());
        shapeRenderer.rect(0f, 0f, 50f, 50f);
        shapeRenderer.rect( (float) rightBtn.getX(), (float) rightBtn.getY(), (float) rightBtn.getWidth(), (float) rightBtn.getHeight());
        shapeRenderer.end();
        sb.end();
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
        shapeRenderer.dispose();
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
        if(screenX >= rightBtn.getX() && screenX <= rightBtn.getX() + rightBtn.getWidth() &&
                (screenHeight - screenY) >= rightBtn.getY() && (screenHeight - screenY) <= (rightBtn.getY() + rightBtn.getHeight())){
            System.out.println("Touch top");
            gsm.set(new PlayState(gsm));
        }
        if(screenX >= leftBtn.getX() && screenX <= leftBtn.getX() + leftBtn.getWidth() &&
                (screenHeight - screenY) >= leftBtn.getY() && (screenHeight - screenY) <= (leftBtn.getY() + leftBtn.getHeight())){
            System.out.println("Touch bot");
        }
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
