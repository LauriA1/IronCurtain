package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.utils.Animation;


public class Runner {
    private static final int MOVEMENT = 500;
    private Texture runner;
    private Vector3 position;
    private Sprite runnerSprite;
    private Animation runnerAnimation;
    private Body body;

    float movementL, movementR;
    int frameCount = 12;
    boolean is_jumping = true;
    float max_health;
    float health;

    public Runner(World world, float x, float y){
        position = new Vector3(x, y, 0);
        runner = new Texture("runner.png");
        runnerSprite = new Sprite(runner);
        runnerSprite.setSize(IronCurtain.screenWidth/IronCurtain.screenHeight * runnerSprite.getWidth() * 0.5f,
                IronCurtain.screenWidth/IronCurtain.screenHeight * runnerSprite.getHeight() * 0.5f);

        runnerAnimation = new Animation(new TextureRegion(runner), frameCount, 10.5f);
        moveRight();

        max_health = 10f;
        health = max_health;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x + getWidth() / 200f, position.y + getHeight() / 200f);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 200f, getHeight() / 200f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.groupIndex = -1;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update(float dt){
        position.x = body.getPosition().x * 100f - getWidth() / 2;
        position.y = body.getPosition().y * 100f - getHeight() / 2;

        if ((position.y + getHeight()) > 0) {
            is_jumping = true;
        }
        else {
            is_jumping = false;
        }

        runnerAnimation.update(dt);
    }

    public void moveRight(){
        movementR = MOVEMENT;
        runnerAnimation.setDirection(0.5f, true, false);
    }

    public void moveLeft(){
        movementL = -MOVEMENT;
        runnerAnimation.setDirection(0.5f, false, false);
    }

    public void keyUpRight(){
        movementR = 0;
        runnerAnimation.setDirection(200, true, true);
    }

    public void keyUpLeft(){
        runnerAnimation.setDirection(200, false, true);
        movementL = 0;
    }

    public float getHeight() { return runnerSprite.getHeight(); }

    public float getWidth() { return runnerSprite.getWidth() / frameCount; }

    public TextureRegion getRunner(boolean is_jumping) {
        return runnerAnimation.getFrame(is_jumping); }

    public Vector3 getPosition() { return position; }

    public void setHealth(float health) {
        if (health > 0) {
            this.health = health;
        }
        else {
            this.health = 0;
        }
    }

    public float getHealth() {
        return health;
    }

    public void decreaseHealth(float value) {
        if ((health - value) > 0) {
            health -= value;
            max_health -= value;
        }
        else {
            health = 0;
        }
    }

    public float getMaxHealth() {
        return max_health;
    }

    public Body getBody() {
        return body;
    }

    public void dispose(){
        runner.dispose();
    }

}