package com.lamoid.ironcurtain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.lamoid.ironcurtain.sprites.Mines;
import com.lamoid.ironcurtain.utils.ScreenShaker;

public class GameScreen implements Screen, InputProcessor {
  	final IronCurtain game;

    SpriteBatch batch;
    Sprite sprite;
    Sprite sprite2;
    Sprite sprite3;
    Texture img;
    World world;
    Body body;
    Body bodyEdgeScreen;
    OrthographicCamera camera;
    Rectangle left_key;
    Rectangle right_key;
    ShapeRenderer shapeRenderer;
    Texture texture;

    private List<Mines> mines;
    ScreenShaker screenShaker;

    boolean moving_left = false;
    boolean moving_right = false;
    boolean is_jumping = true;
    boolean is_frozen = false;
    boolean facing_left = false;

    boolean do_not_shake = false;

    int old_screenX = 0;
    int old_screenY = 0;
    int left_pointer = 0;
    int right_pointer = 0;

    int mine_count = 3;

    final float PIXELS_TO_METERS = 100f;

	public GameScreen(final IronCurtain gam) {
		this.game = gam;

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);
        sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);

        img = new Texture ("mine.png");
        sprite3 = new Sprite(img);
        sprite3.setPosition(1024, (Gdx.graphics.getHeight() / 2) * -1);

        left_key = new Rectangle();
        left_key.x = Gdx.graphics.getWidth() * 0.05f;
        left_key.y = left_key.x;
        left_key.width = left_key.x * 2;
        left_key.height = left_key.width;

        right_key = new Rectangle();
        right_key.x = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.05f) - sprite.getWidth();
        right_key.y = left_key.y;
        right_key.width = left_key.width;
        right_key.height = left_key.height;

        shapeRenderer = new ShapeRenderer();

        texture = new Texture(Gdx.files.internal("background.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        sprite2 = new Sprite(texture);
        sprite2.setOrigin(0,0);
        sprite2.setScale(3);
        sprite2.setPosition(-(Gdx.graphics.getWidth() / 2),-((Gdx.graphics.getHeight() / 2) + 8));

        world = new World(new Vector2(0, -10f),true);

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
        fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef);
        shape.dispose();

        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        float w = (sprite2.getWidth() * 3) / 100f;
        // Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS- 50/PIXELS_TO_METERS;
        //bodyDef2.position.set(0,
//                h-10/PIXELS_TO_METERS);
        bodyDef2.position.set(0,0);
        FixtureDef fixtureDef2 = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w/2,-h/2, w/2,-h/2);
        fixtureDef2.shape = edgeShape;

        bodyEdgeScreen = world.createBody(bodyDef2);
        bodyEdgeScreen.createFixture(fixtureDef2);
        edgeShape.dispose();

        mines = new ArrayList<Mines>();

        /*for (int i = 0; i < 10 ; i++ ) {
            mines.add(new Mines(sprite2.getX(), sprite2.getX() + sprite2.getWidth() * 3, (Gdx.graphics.getHeight() / 2) * -1));
        }*/

        int i = 0;
        float x1 = sprite2.getX();
        float x2 = sprite2.getX() + sprite2.getWidth() * 3;
        float y = (Gdx.graphics.getHeight() / 2) * -1;

        while (i < mine_count) {
            mines.add(new Mines(x1, x2, y));
            int x = mines.get(i).getX();
            boolean is_overlapping = false;

            if (i > 0) {
                for (Mines mine : mines) {
                    if (Math.abs(mine.getX() - x) < 256 && mines.indexOf(mine) != i) {
                        is_overlapping = true;
                        //System.out.println("Overlapping mine #" + i);
                    }
                }

                if (is_overlapping) {
                    mines.remove(i);
                }

                if (mines.size() > i) {
                    i++;
                }
            }
            else {
                i++;
            }
        }

        Gdx.input.setInputProcessor(this);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());

        screenShaker = new ScreenShaker(camera, 8);
	}

	@Override
	public void render(float delta) {
	    //System.out.println(sprite.getX() + " | " + bodyEdgeScreen.getPosition().x);

        if (!do_not_shake) {
            camera.position.set(sprite.getX() + sprite.getWidth() / 2, 0, 0);
        }
        screenShaker.update();
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);

        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.getWidth()/2,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        sprite2.draw(batch);
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());

        for (Mines mine : mines) {
            mine.drawMine(batch);
        }

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(80 / 255.0f, 80 / 255.0f, 50 / 255.0f, 1);
        shapeRenderer.rect(left_key.x, left_key.y, left_key.width, left_key.height);
        shapeRenderer.rect(right_key.x, right_key.y, right_key.width, right_key.height);
        shapeRenderer.end();

        if (moving_left && !is_frozen) {
            body.setLinearVelocity(-7.5f, body.getLinearVelocity().y);
        }
        else if (moving_right && !is_frozen) {
            body.setLinearVelocity(7.5f, body.getLinearVelocity().y);
        }

        if (is_jumping && (body.getPosition().y <= -5.5)) {
            if (!moving_left && !moving_right) {
                body.setLinearVelocity(0f, 0f);
            }
            body.setLinearVelocity(body.getLinearVelocity().x, 0f);
            is_jumping = false;
            is_frozen = false;
        }

        for (Mines mine : mines) {
            if (sprite.getBoundingRectangle().overlaps(mine.getSprite().getBoundingRectangle())) {
                /*if (facing_left) {
                    body.setLinearVelocity(5f, 0f);
                }
                else {
                    body.setLinearVelocity(-5f, 0f);
                }
                is_jumping = true;
                is_frozen = true;*/
                if (!screenShaker.get_status() && !do_not_shake) {
                    screenShaker.shake(30, camera.position);
                    do_not_shake = true;
                }
            }
            else {
                if (do_not_shake) {
                    do_not_shake = false;
                }
            }
        }
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		//rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
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
	    //System.out.println("touchDown registered!");
        if (!is_frozen) {
            if (screenX >= left_key.x && screenX <= (left_key.x + left_key.width) //left
                    && (Gdx.graphics.getHeight() - screenY) >= left_key.y && (Gdx.graphics.getHeight() - screenY) <= (left_key.y + left_key.height)) {
                if (moving_right) {
                    body.setLinearVelocity(0f, body.getLinearVelocity().y);
                }
                facing_left = true;
                moving_left = true;
                left_pointer = pointer;
            } else if (screenX >= right_key.x && screenX <= (right_key.x + right_key.width) //right
                    && (Gdx.graphics.getHeight() - screenY) >= right_key.y && (Gdx.graphics.getHeight() - screenY) <= (right_key.y + right_key.height)) {
                if (moving_left) {
                    body.setLinearVelocity(0f, body.getLinearVelocity().y);
                }
                facing_left = false;
                moving_right = true;
                right_pointer = pointer;
            } else {
                old_screenX = screenX;
                old_screenY = screenY;
            }
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

        if (!moving_left && !moving_right && is_jumping && !is_frozen) {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        if (!moving_left && !moving_right && !is_jumping && !is_frozen) {
            body.setLinearVelocity(0f, 0f);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if ((old_screenY - screenY) > 256 && (old_screenX - screenX) < 256 && !is_jumping) {
            body.setLinearVelocity(body.getLinearVelocity().x, 10f);
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
}
