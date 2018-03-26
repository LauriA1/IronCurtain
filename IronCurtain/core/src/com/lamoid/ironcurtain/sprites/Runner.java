package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;



public class Runner {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 500;
    private Texture runner;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Sprite runnerSprite;
    private Animation runnerAnimation;

    boolean lastDirection = true;
    float movementL, movementR;
    int index;

    public Runner(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0,0,0);
        runner = new Texture("rsz_run_2.png");
        runnerSprite = new Sprite(runner);

        runnerAnimation = new Animation(new TextureRegion(runner), 12, 10.5f);
        bounds = new Rectangle(x, y, runner.getWidth(), runner.getHeight());
        System.out.println(runner.getHeight() + " : " + runner.getWidth());
    }

    public void update(float dt){
        runnerAnimation.update(dt);
        if(position.y > 0)
            velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add((movementL + movementR) * dt,velocity.y, 0);
        if(position.y < 30)
            position.y = 30;

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public void jump(){
        if(position.y < 35)
            velocity.y = 300;
    }

    public void moveRight(){
        movementR = MOVEMENT;
        runnerAnimation.setDirection(0.5f, true);
    }

    public void moveLeft(){
        movementL = -MOVEMENT;
        runnerAnimation.setDirection(0.5f, false);
    }

    public void keyUpRight(){
        movementR = 0;
        runnerAnimation.setDirection(200, true);
    }

    public void keyUpLeft(){
        runnerAnimation.setDirection(200, false);
        movementL = 0;
    }

    public float getHeight() { return runnerSprite.getHeight(); }

    public float getWidth() { return runnerSprite.getWidth(); }

   // public Sprite getRunner() { return runnerSprite; }
    public TextureRegion getRunner() {
        return runnerAnimation.getFrame(); }


    public Vector3 getPosition() { return position; }

    public void dispose(){
        runner.dispose();
    }


}
