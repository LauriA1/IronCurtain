package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.utils.Animation;


public class Runner {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 500;
    private Texture runner;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Sprite runnerSprite;
    private Animation runnerAnimation;
    private Body body;

    boolean lastDirection = true;
    float movementL, movementR;
    int index;
    int frameCount = 12;
    boolean is_jumping = true;
    float health;

    public Runner(World world, float x, float y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0,0,0);
        runner = new Texture("runner.png");
        runnerSprite = new Sprite(runner);
        runnerSprite.setSize(Gdx.graphics.getWidth()/Gdx.graphics.getHeight() * runnerSprite.getWidth() * 0.95f,
                Gdx.graphics.getWidth()/Gdx.graphics.getHeight() * runnerSprite.getHeight() * 0.95f);

        runnerAnimation = new Animation(new TextureRegion(runner), frameCount, 10.5f);
        bounds = new Rectangle(x, y, runner.getWidth(), runner.getHeight());
        //System.out.println(runner.getHeight() + " : " + runner.getWidth());

        health = 10f;

        //create body
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
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.groupIndex = -1;

                body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update(float dt){
        position.x = body.getPosition().x * 100f - getWidth() / 2;
        position.y = body.getPosition().y * 100f - getHeight() / 2;

        //System.out.println(position.y);

        if ((position.y + getHeight()) > 0) {
            is_jumping = true;
        }
        else {
            is_jumping = false;
        }

        runnerAnimation.update(dt);

        /*sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.getWidth()/2,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );*/

        /*if(position.y > 0)
            velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add((movementL + movementR) * dt,velocity.y, 0);
        if(position.y < 30)
            position.y = 30;

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);*/
    }

    public void jump(){
        if(position.y < 35)
            velocity.y = 300;

        //runnerAnimation.setDirection(0.5f,true, false);
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

    public Sprite getRunnerSprite() { return runnerSprite; }
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

    public Body getBody() {
        return body;
    }

    public void dispose(){
        runner.dispose();
    }


}
