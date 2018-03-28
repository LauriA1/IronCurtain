package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Mines {
    private Texture texture;
    private Sprite sprite;
    private Body body;

    private int x;

    public Mines(World world, float x1, float x2, float y) {
        texture = new Texture ("mine.png");
        sprite = new Sprite(texture);

        Random rand;
        rand = new Random();
        x = rand.nextInt(((int)x2 - (int)x1) + 1) + (int)x1;

        sprite.setPosition(x, y);

        //create body
        BodyDef bodyDef3 = new BodyDef();
        bodyDef3.type = BodyDef.BodyType.StaticBody;
        bodyDef3.position.set((sprite.getX() + sprite.getWidth() / 2) / 100f, (sprite.getY() + sprite.getHeight() / 2) / 100f);
        body = world.createBody(bodyDef3);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / 100f, sprite.getHeight() / 2 / 100f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void drawMine(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public int getX() {
        return x;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
