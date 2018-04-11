package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.lamoid.ironcurtain.IronCurtain;

public class Map {
    private Texture texture;
    private Sprite[] sprites;
    private Body body;

    private int mapLength = 10;

    public Map(World world) {
        sprites = new Sprite[6];

        for (int i = (sprites.length - 1); i > -1; i--) {
            texture = new Texture ("map1_layer" + (i + 1) + ".png");
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            TextureRegion textureRegion = new TextureRegion(texture);
            textureRegion.setRegion(0, 0, texture.getWidth()*mapLength, texture.getHeight());

            sprites[i] = new Sprite(textureRegion);
            System.out.println("NÄYTÖN SUHDE: " + (IronCurtain.screenWidth/IronCurtain.screenHeight));
            System.out.println("NÄYTÖN LEVEYS: " + IronCurtain.screenWidth);
            System.out.println("NÄYTÖN KORKEUS: " + IronCurtain.screenHeight);
            System.out.println("ENNEN     WIDTH: " + sprites[i].getWidth() + "HEIGHT: " + sprites[i].getHeight());
            sprites[i].setSize(IronCurtain.screenWidth/IronCurtain.screenHeight * sprites[i].getWidth() * 0.5f,
                    IronCurtain.screenWidth/IronCurtain.screenHeight * sprites[i].getHeight() * 0.5f);
            System.out.println("JÄLKEEN     WIDTH: " + sprites[i].getWidth() + "HEIGHT: " + sprites[i].getHeight());
            sprites[i].setOrigin(0, 0);

            float y;
            if (i + 1 >= 5) {
                y = ((IronCurtain.screenHeight + 64) / 2) - sprites[i].getHeight();
            }
            else {
                y = -((IronCurtain.screenHeight + 64) / 2);
            }
            sprites[i].setPosition(-(IronCurtain.screenWidth / 2), y);
        }

        BodyDef bodyDef3 = new BodyDef();

        bodyDef3.type = BodyDef.BodyType.StaticBody;

        float w = sprites[0].getWidth() / 100f; //Gdx.graphics.getWidth()/100f;
        float h = IronCurtain.screenHeight / 100f;

        bodyDef3.position.set(0f, 0.64f);
        FixtureDef fixtureDef3 = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-(IronCurtain.screenWidth / 100f)/2, -h/2, w, -h/2);
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

    public void animateBackground(Camera camera, float old_cameraX) {
        for (int i = (sprites.length - 2); i > 0; i--) {
            if (camera.position.x > old_cameraX) {
                sprites[i].setPosition(sprites[i].getX() + ((sprites.length - i)), sprites[i].getY());
            }
            else if (camera.position.x < old_cameraX) {
                sprites[i].setPosition(sprites[i].getX() - ((sprites.length - i)), sprites[i].getY());
            }
        }
    }

    public Sprite getSprite() {
        return sprites[0];
    }

    public float getLength() {
        return sprites[5].getWidth();
    }

    public Body getBody() {
        return body;
    }

}
