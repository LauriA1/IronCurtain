package com.lamoid.ironcurtain.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.utils.Animation;

public class Explosions {
    private Texture texture;
    private Animation explosionAnimation;
    private Sprite sprite;

    private int frameCount = 16;

    Vector2 position;

    public Explosions (Vector2 position) {
        texture = new Texture ("explosion.png");

        sprite = new Sprite(texture);
        sprite.setSize(IronCurtain.screenWidth/IronCurtain.screenHeight * sprite.getWidth() * 0.75f,
                IronCurtain.screenWidth/ IronCurtain.screenHeight * sprite.getHeight() * 0.75f);

        explosionAnimation = new Animation(new TextureRegion(texture), frameCount, 2f);
        this.position = position;
    }

    public void update(float dt){
        explosionAnimation.update(dt);
    }

    public TextureRegion getExplosion() {
        return explosionAnimation.getFrame(false);
    }

    public Vector2 getPosition() { return position; }

    public float getHeight() { return sprite.getHeight(); }

    public float getWidth() { return sprite.getWidth() / frameCount; }

    public boolean getAnimationFinished() {
        if (explosionAnimation.getFrameNumber() == frameCount - 1) {
            return true;
        }
        else {
            return false;
        }
    }
}
