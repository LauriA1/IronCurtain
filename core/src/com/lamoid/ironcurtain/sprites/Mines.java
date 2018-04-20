package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.utils.Animation;

import java.util.Random;

public class Mines {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    private Animation mineAnimation;

    private int frameCount = 2;

    private int r;

    private float x = 0;
    private float y = 0;

    private Vector3 position;

    public Mines(World world, float x1, float x2, float y) {
        position = new Vector3(x, y, 0);

        texture = new Texture ("mine.png");

        Random rand;
        rand = new Random();

        r = rand.nextInt(((int)x2 - (int)x1) + 1) + (int)x1;

        position.x = (float)r / 100f;
        position.y = (y + IronCurtain.screenHeight * 0.0375f) / 100f;

        sprite = new Sprite(texture);
        sprite.setSize(IronCurtain.screenWidth/IronCurtain.screenHeight * sprite.getWidth() * 0.5f,
                IronCurtain.screenWidth/ IronCurtain.screenHeight * sprite.getHeight() * 0.5f);

        mineAnimation = new Animation(new TextureRegion(texture), frameCount, 1.5f);

        //create body
        BodyDef bodyDef3 = new BodyDef();
        bodyDef3.type = BodyDef.BodyType.StaticBody;
        bodyDef3.position.set(position.x + getWidth() / 200f, position.y + getHeight() / 200f);
        body = world.createBody(bodyDef3);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 200f, getHeight() / 200f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = -2;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void update(float dt){
        mineAnimation.update(dt);
    }

    public TextureRegion getMine() {
        return mineAnimation.getFrame(false); }

    public Vector3 getPosition() { return position; }

    public float getHeight() { return sprite.getHeight(); }

    public float getWidth() { return sprite.getWidth() / frameCount; }

    public Sprite getSprite() {
        return sprite;
    }
}
