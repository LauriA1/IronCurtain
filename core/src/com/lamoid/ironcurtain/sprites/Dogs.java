package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.utils.Animation;

public class Dogs {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    private FixtureDef fixtureDef;
    private Animation dogAnimation;

    private Vector3 position;
    float movementL, movementR;

    private boolean sound_played = false;

    private float x = 0;
    private float y = 0;

    private static final int MOVEMENT = 500;

    private int frameCount = 7;

    public Dogs(World world, Camera camera) {
        position = new Vector3(x, y, 0);

        texture = new Texture("dog.png");

        position.x = camera.position.x / 100f + (IronCurtain.screenWidth * 1.5f) / 200f;
        position.y = (-IronCurtain.screenHeight * 0.8f) / 200f;

        sprite = new Sprite(texture);
        sprite.setSize(IronCurtain.screenWidth/ IronCurtain.screenHeight * sprite.getWidth() * 0.6f,
                IronCurtain.screenWidth/IronCurtain.screenHeight * sprite.getHeight() * 0.6f);

        dogAnimation = new Animation(new TextureRegion(texture), frameCount, 10.5f);
        //sprite.setPosition(x, y);
        //sprite.flip(true, false);

        //create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x + getWidth() / 200f, position.y + getHeight() / 200f);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 200f, getHeight() / 200f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.groupIndex = -2;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update(float dt){
        /*sprite.setPosition(body.getPosition().x * 100f - sprite.getWidth() / 2,
                body.getPosition().y * 100f - sprite.getHeight() / 2);*/

        position.x = body.getPosition().x * 100f - getWidth() / 2;
        position.y = body.getPosition().y * 100f - getHeight() / 2;

        dogAnimation.update(dt);
    }

    public void moveRight(){
        movementR = MOVEMENT;
        dogAnimation.setDirection(0.5f, true, false);
    }

    public void moveLeft(){
        movementL = -MOVEMENT;
        dogAnimation.setDirection(0.5f, false, false);
    }

    /*public void resetPos(Camera camera) {
        x = camera.position.x / 100f - (IronCurtain.screenWidth * 1.5f) / 200f;
        y = (-IronCurtain.screenHeight * 0.8f) / 200f;
        body.setTransform(x, y, body.getAngle());
    }*/

    public void drawDog(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public TextureRegion getDog() {
        return dogAnimation.getFrame(false); }

    public void set_sound_played(boolean played) {
        sound_played = played;
    }

    public boolean get_sound_played() {
        return sound_played;
    }

    public Vector3 getPosition() { return position; }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHeight() { return sprite.getHeight(); }

    public float getWidth() { return sprite.getWidth() / frameCount; }

    public Body getBody() {
        return body;
    }

    public FixtureDef getFixture() { return fixtureDef; }

    public Texture getTexture() {
        return texture;
    }
}
