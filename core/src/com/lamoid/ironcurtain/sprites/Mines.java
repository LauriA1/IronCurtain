package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Mines {
    private Texture texture;
    private Sprite sprite;

    private int x;

    public Mines(float x1, float x2, float y) {
        texture = new Texture ("mine.png");
        sprite = new Sprite(texture);

        Random rand;
        rand = new Random();
        x = rand.nextInt(((int)x2 - (int)x1) + 1) + (int)x1;

        sprite.setPosition(x, y);
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
