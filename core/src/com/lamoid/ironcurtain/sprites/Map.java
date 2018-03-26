package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Map {
    private Texture texture;
    private Sprite[] sprites;
    private Body body;

    public Map(World world) {
        sprites = new Sprite[6];

        for (int i = (sprites.length - 1); i > -1; i--) {
            texture = new Texture ("map1_layer" + (i + 1) + ".png");
            sprites[i] = new Sprite(texture);
            sprites[i].setOrigin(0, 0);

            float y;
            if (i + 1 >= 5) {
                y = (Gdx.graphics.getHeight() / 2) - sprites[i].getHeight();
            }
            else {
                y = -((Gdx.graphics.getHeight() / 2));
            }
            sprites[i].setPosition(-(Gdx.graphics.getWidth() / 2), y);
        }

        BodyDef bodyDef3 = new BodyDef();

        bodyDef3.type = BodyDef.BodyType.StaticBody;

        float w = sprites[0].getWidth() / 100f; //Gdx.graphics.getWidth()/100f;
        float h = Gdx.graphics.getHeight() / 100f;

        bodyDef3.position.set(2.56f,0.64f);
        FixtureDef fixtureDef3 = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w/2,-h/2,w/2,-h/2);
        fixtureDef3.shape = edgeShape;

        body = world.createBody(bodyDef3);
        body.setUserData(this);
        body.createFixture(fixtureDef3);
        edgeShape.dispose();
    }

    public void drawMap(SpriteBatch batch) {
        for (int i = (sprites.length - 1); i > -1; i--) {
            sprites[i].draw(batch);
        }
    }

    public Body getBody() {
        return body;
    }

}
