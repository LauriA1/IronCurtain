package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Towers {
    private Texture texture;
    private Sprite sprite;


    private int x;
    private int i = 0;
    private float t = 0;
    private boolean check = true;

    private int overlap_count = 0;

    private float _x1, _y1, _x2, _y2, _x3, _y3;

    public Towers(float x1, float x2, float y) {
        texture = new Texture ("tower.png");
        sprite = new Sprite(texture);
        sprite.setSize(Gdx.graphics.getWidth()/Gdx.graphics.getHeight() * sprite.getWidth() * 0.95f,
                Gdx.graphics.getWidth()/Gdx.graphics.getHeight() * sprite.getHeight() * 0.95f);

        Random rand;
        rand = new Random();
        x = rand.nextInt(((int)x2 - (int)x1) + 1) + (int)x1;
        i = rand.nextInt((100 - (-100)) + 1) + (-100);
        t = rand.nextInt((5 - 2) + 1) + 2;

        sprite.setPosition(x, y);
    }

    public void drawSpotlight(ShapeRenderer shapeRenderer, Camera camera) {
        shapeRenderer.setColor(255 / 255.0f, 255 / 255.0f, 153 / 255.0f, 0.3f);
        /*shapeRenderer.cone(sprite.getX() + sprite.getWidth() / 3 + Gdx.graphics.getWidth() / 2 - camera.position.x,
                sprite.getY() + Gdx.graphics.getHeight(),
                0, 50, 200, 20);*/

        _x1 = sprite.getX() + sprite.getWidth() / 2 + Gdx.graphics.getWidth() / 2 - camera.position.x;
        _y1 = (sprite.getY() + sprite.getHeight() + Gdx.graphics.getHeight() / 2) * 0.85f;
        //_y1 = sprite.getY() + Gdx.graphics.getHeight() * 0.95f;
        _x2 = _x1 + Gdx.graphics.getWidth() * 0.05f;
        _y2 = _y1 - sprite.getHeight() * 0.75f;
        _x3 = _x1 - Gdx.graphics.getWidth() * 0.05f;
        _y3 = _y2;

        if (check) {
            if (i < 100) {
                i++;
            } else {
                check = false;
            }
        } else {
            if (i > -100) {
                i--;
            } else {
                check = true;
            }
        }

        _x2 += i * 2;
        _x3 += i * 2;

        shapeRenderer.triangle(_x1, _y1, _x2, _y2, _x3, _y3);
        shapeRenderer.setColor(255 / 255.0f, 255 / 255.0f, 153 / 255.0f, 1f);
        shapeRenderer.ellipse(_x3, _y3 - ((_x2 - _x3)/6)/2, _x2 - _x3, (_x2- _x3)/6);
        //System.out.println((sprite.getX()) + ", " + (sprite.getY() + Gdx.graphics.getHeight()));
    }

    public void drawGunfire(ShapeRenderer shapeRenderer, Camera camera) {
        shapeRenderer.setColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 0.5f);

        Random rand;
        rand = new Random();
        int r = rand.nextInt((64 - (-64)) + 1) + (-64);

        _x2 = _x1 + r;
        _y2 = _y1 - sprite.getHeight() * 0.75f + Math.abs(r);

        shapeRenderer.rectLine(_x1, _y1, _x2, _y2, 4);
    }

    public void drawTower(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public int getX() {
        return x;
    }

    public float getT() {
        return t;
    }

    public void set_overlap_count(int overlap_count) {
        this.overlap_count = overlap_count;
    }

    public int get_overlap_count() {
        return overlap_count;
    }

    public List<Float> getCoordinates() {
        List<Float> coordinates = new ArrayList<Float>();
        //coordinates.add(_x1);
        //coordinates.add(_y1);
        coordinates.add(_x2);
        //coordinates.add(_y2);
        coordinates.add(_x3);
        //coordinates.add(_y3);
        return coordinates;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
