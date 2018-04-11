package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.IronCurtain;

public class Dogs {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    private FixtureDef fixtureDef;

    private float x = 0;
    private float y = 0;

    public Dogs(World world, Camera camera) {
        texture = new Texture("dog.png");

        x = camera.position.x / 100f - (IronCurtain.screenWidth * 1.5f) / 200f;
        y = (-IronCurtain.screenHeight * 0.8f) / 200f;

        sprite = new Sprite(texture);
        sprite.setSize(IronCurtain.screenWidth/ IronCurtain.screenHeight * sprite.getWidth() * 0.5f,
                IronCurtain.screenWidth/IronCurtain.screenHeight * sprite.getHeight() * 0.5f);
        sprite.setPosition(x, y);

        //create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x + sprite.getWidth() / 200f, y + sprite.getHeight() / 200f);

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
        shape.dispose();
    }

    public void updatePos() {
        sprite.setPosition(body.getPosition().x * 100f - sprite.getWidth() / 2,
                body.getPosition().y * 100f - sprite.getHeight() / 2);
    }

    public void resetPos(Camera camera) {
        x = camera.position.x / 100f - (IronCurtain.screenWidth * 1.5f) / 200f;
        y = (-IronCurtain.screenHeight * 0.8f) / 200f;
        body.setTransform(x, y, body.getAngle());
    }

    public void drawDog(SpriteBatch batch) {
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
