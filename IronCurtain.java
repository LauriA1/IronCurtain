package com.lamoid.ironcurtain;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class IronCurtain extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;
    Rectangle left_key;
    Rectangle right_key;
    Rectangle up_key;
    ShapeRenderer shapeRenderer;

    float torque = 0.0f;
    boolean drawSprite = true;

    float screen_width = 0;
    float screen_height = 0;

    int left_pointer = 0;
    int right_pointer = 0;

    boolean moving_left = false;
    boolean moving_right = false;

    final float PIXELS_TO_METERS = 100f;

    @Override
    public void create() {
        screen_width = Gdx.graphics.getWidth();
        screen_height = Gdx.graphics.getHeight();

        left_key = new Rectangle();
        left_key.x = 128;
        left_key.y = 128;
        left_key.width = 256;
        left_key.height = 256;

        right_key = new Rectangle();
        right_key.x = screen_width - 384;
        right_key.y = 128;
        right_key.width = 256;
        right_key.height = 256;

        up_key = new Rectangle();
        up_key.x = screen_width - 384;
        up_key.y = 512;
        up_key.width = 256;
        up_key.height = 256;

        shapeRenderer = new ShapeRenderer();

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);

        sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);

        world = new World(new Vector2(0, 0f),true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
                        PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;

        body.createFixture(fixtureDef);
        shape.dispose();

        Gdx.input.setInputProcessor(this);

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());
    }

    private float elapsed = 0;
    @Override
    public void render() {
        camera.update();

        world.step(1f/60f, 6, 2);

        body.applyTorque(torque,true);

        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
                        getWidth()/2 ,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );

        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(80 / 255.0f, 80 / 255.0f, 50 / 255.0f, 1);
        shapeRenderer.rect(left_key.x, left_key.y, left_key.width, left_key.height);
        shapeRenderer.rect(right_key.x, right_key.y, right_key.width, right_key.height);
        shapeRenderer.rect(up_key.x, up_key.y, up_key.width, up_key.height);
        shapeRenderer.end();

        batch.begin();

        if(drawSprite)
            batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
                    sprite.getOriginY(),
                    sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
                            getScaleY(),sprite.getRotation());

        batch.end();

        debugRenderer.render(world, debugMatrix);
    }

    @Override
    public void dispose() {
        img.dispose();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
	    return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //System.out.println("screenY: " + screenY + " | left_key.y: " + left_key.y);

        if (screenX >= left_key.x && screenX <= (left_key.x + left_key.width) //left
                && (screen_height - screenY) >= left_key.y && (screen_height - screenY) <= (left_key.y + left_key.height)) {
            if (moving_right) {
                body.setLinearVelocity(0f, 0f);
            }
            body.applyLinearImpulse(new Vector2(-5f, 0), body.getWorldCenter(), true);
            moving_left = true;
            left_pointer = pointer;
        }

        if (screenX >= right_key.x && screenX <= (right_key.x + right_key.width) //right
                && (screen_height - screenY) >= right_key.y && (screen_height - screenY) <= (right_key.y + right_key.height)) {
            if (moving_left) {
                body.setLinearVelocity(0f, 0f);
            }
            body.applyLinearImpulse(new Vector2(5f, 0), body.getWorldCenter(), true);
            moving_right = true;
            right_pointer = pointer;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (moving_left && pointer == left_pointer) {
            moving_left = false;
        }

        if (moving_right && pointer == right_pointer) {
            moving_right = false;
        }

        if (!moving_left && !moving_right) {
            body.setLinearVelocity(0f, 0f);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
