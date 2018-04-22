package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.IronCurtain;

import java.util.Random;

public class Missile {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    private FixtureDef fixtureDef;

    private float x = 0;
    private float y = 0;

    public Missile(World world) {
        texture = new Texture("missile.png");

        y = -Gdx.graphics.getHeight();

        sprite = new Sprite(texture);
        sprite.setPosition(x, y);

        //create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x + sprite.getWidth() / 200f, y + sprite.getHeight() / 200f);
        //bodyDef.angularVelocity = 5;
        //bodyDef.angularDamping = 5;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 200f, sprite.getHeight() / 200f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.groupIndex = -2;

        body.createFixture(fixtureDef);
        body.setGravityScale(0);

        shape.dispose();
    }

    public void updatePos() {
        sprite.setRotation(body.getAngle());
        sprite.setPosition(body.getPosition().x * 100f - sprite.getWidth() / 2,
                body.getPosition().y * 100f - sprite.getHeight() / 2);
    }

    public void setPos(Camera camera) {
        Random rand;
        rand = new Random();

        if ((rand.nextInt(((int)2 - (int)1) + 1) + (int)1) == 1) {
            x = camera.position.x / 100f;
        }
        else {
            x = camera.position.x / 100f + IronCurtain.screenWidth / 100f;
        }

        y = Gdx.graphics.getHeight() / 200f;
        body.setTransform(x, y, body.getAngle());
    }

    public void drawMissile(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Body getBody() {
        return body;
    }

    public FixtureDef getFixture() { return fixtureDef; }

    public Texture getTexture() {
        return texture;
    }
}
