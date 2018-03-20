package com.lamoid.ironcurtain;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class IronCurtain implements ApplicationListener, InputProcessor {
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Texture player_texture, background_texture;
    private Sprite player_sprite, background_sprite;

    private float screenWidth, screenHeight;
    //private float touchX, touchY;

    private World world;
    private Body body;
    private Body bodyEdgeScreen;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;

    private float torque = 0.0f;
    private boolean drawSprite = true;
    private final float PIXELS_TO_METERS = 100f;

    private boolean is_jumping = false;
    private boolean moving_left = false;
    private boolean moving_right = false;
    private int left_pointer, right_pointer;
    private int old_screenX, old_screenY;
	
	@Override
	public void create () {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

        background_texture = new Texture(Gdx.files.internal("img/background.jpg"));
        background_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background_sprite = new Sprite(background_texture);
        background_sprite.setOrigin(0,0);
        background_sprite.setPosition(-background_sprite.getWidth()/2,-background_sprite.getHeight()/2);

		player_texture = new Texture(Gdx.files.internal("img/running-man.png"));
        player_sprite = new Sprite(player_texture);
        player_sprite.setSize(100, 100);
        player_sprite.setPosition(-player_sprite.getWidth()/2, -player_sprite.getHeight()/2);

        world = new World(new Vector2(0, -10f),true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((player_sprite.getX() + player_sprite.getWidth()/2) / PIXELS_TO_METERS,
                (player_sprite.getY() + player_sprite.getHeight()/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(player_sprite.getWidth()/2 / PIXELS_TO_METERS,
                player_sprite.getHeight()/2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        //fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef);
        shape.dispose();

        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS + 50/PIXELS_TO_METERS;
        bodyDef2.position.set(0,0);
        FixtureDef fixtureDef2 = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w/2, -h/2, w/2, -h/2);
        fixtureDef2.shape = edgeShape;

        bodyEdgeScreen = world.createBody(bodyDef2);
        bodyEdgeScreen.createFixture(fixtureDef2);
        edgeShape.dispose();

        Gdx.input.setInputProcessor(this);

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(screenWidth, screenHeight);
    }

    @Override
	public void render () {
	    handleInput();
        camera.position.set(player_sprite.getX() + player_sprite.getWidth() / 2,
                player_sprite.getY() + player_sprite.getHeight()/2, 0);
        camera.update();
        world.step(1f/60f, 6, 2);

        body.applyTorque(torque,true);

        player_sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - player_sprite.getWidth()/2,
                (body.getPosition().y * PIXELS_TO_METERS) - player_sprite.getHeight()/2);
        player_sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        batch.begin();
        if(drawSprite)
        {
            background_sprite.draw(batch);
            batch.draw(player_sprite, player_sprite.getX(), player_sprite.getY(),
                    player_sprite.getOriginX(), player_sprite.getOriginY(),
                    player_sprite.getWidth(), player_sprite.getHeight(),
                    player_sprite.getScaleX(), player_sprite.getScaleY(),
                    player_sprite.getRotation());
        }
        batch.end();

        debugRenderer.render(world, debugMatrix);
	}

    private void handleInput()
    {
        if (moving_left) {
            body.setLinearVelocity(-1f, body.getLinearVelocity().y);
        }
        else if (moving_right) {
            body.setLinearVelocity(1f, body.getLinearVelocity().y);
        }

        if (is_jumping && (body.getPosition().y <= -3.57)) {
            if (!moving_left && !moving_right) {
                body.setLinearVelocity(0f, 1f);
            }
            is_jumping = false;
        }
/*
        if(player_sprite.getY() < -332)
        {
            canJump = true;
        }

        if(Gdx.input.isTouched())
        {
            touchX = Gdx.input.getX();
            touchY = Gdx.input.getY();

            if (touchX < screenWidth / 4)                           //left half
            {
                body.setLinearVelocity(-1f, 0f);
            }
            else if (touchX > screenWidth * 3 / 4)                  //right half
            {
                body.setLinearVelocity(1f, 0f);
            }
        }
*/
/*
            if (touchY > screenHeight / 2)                        //bottom half
            {

            }
            else if (touchY < screenHeight / 2)                     //top half
            {
                if (canJump)
                {
                    body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y + 5f);
                    canJump = false;
                }
            }
*/
    }
	
	@Override
	public void dispose () {
		//batch.dispose();
		background_texture.dispose();
		player_texture.dispose();
		world.dispose();
	}

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
        if (screenX <= screenWidth / 4)          //left
        {
            if (moving_right) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }
            moving_left = true;
            left_pointer = pointer;
        }
        else if (screenX >= screenWidth * 3 / 4) //right
        {
            if (moving_left) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }
            moving_right = true;
            right_pointer = pointer;
        }
        else {
            old_screenX = screenX;
            old_screenY = screenY;
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

        if (!moving_left && !moving_right && is_jumping) {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        if (!moving_left && !moving_right && !is_jumping) {
            body.setLinearVelocity(0f, 0f);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if ((old_screenY - screenY) > 256 && (old_screenX - screenX) < 256 && !is_jumping) {
            body.setLinearVelocity(body.getLinearVelocity().x, 5f);
            is_jumping = true;
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /*
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (canJump)
        {
            body.setLinearVelocity(body.getLinearVelocity().x, 5f);
            canJump = false;
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int what, int what2) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }
    */
}