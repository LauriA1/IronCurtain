package com.lamoid.ironcurtain.screens;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.sprites.Mines;
import com.lamoid.ironcurtain.sprites.Runner;
import com.lamoid.ironcurtain.utils.ScreenShaker;
import com.lamoid.ironcurtain.sprites.Map;

public class GameScreen implements Screen, InputProcessor {
  	private final IronCurtain game;

    private SpriteBatch batch;
    private Sprite mine_sprite;
    private Texture img;
    private World world;
    private OrthographicCamera camera;
    private Rectangle left_key, right_key;
    private ShapeRenderer shapeRenderer;
    private Runner runner;

    private List<Mines> mines;
    private ScreenShaker screenShaker;
    private Map map;

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

    int mine_count = 15;

    final float PIXELS_TO_METERS = 100f;

	public GameScreen(final IronCurtain gam) {
		this.game = gam;

        batch = new SpriteBatch();

        img = new Texture ("mine.png");
        mine_sprite = new Sprite(img);
        mine_sprite.setPosition(1024, (Gdx.graphics.getHeight() / 2) * -1);

        left_key = new Rectangle();
        left_key.x = Gdx.graphics.getWidth() * 0.05f;
        left_key.y = left_key.x;
        left_key.width = left_key.x * 2;
        left_key.height = left_key.width;

        right_key = new Rectangle();
        right_key.x = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.15f);
        right_key.y = left_key.y;
        right_key.width = left_key.width;
        right_key.height = left_key.height;

        shapeRenderer = new ShapeRenderer();

        world = new World(new Vector2(0, -20f),true);

        map = new Map(world);

        runner = new Runner(world, 0, 0);

        mines = new ArrayList<Mines>();

        float x1 = map.getSprite().getX();
        float x2 = x1 + map.getSprite().getWidth();
        float y = (Gdx.graphics.getHeight() / 2) * -1;

        int i = 0;

        while (i < mine_count) {
            mines.add(new Mines(world, x1, x2, y));
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
            } else {
                i++;
            }
        }

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.update();

        screenShaker = new ScreenShaker(camera, 32);

        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                System.out.println("beginContact" + "between " + fixtureA.toString() + " and " + fixtureB.toString());
                System.out.println(fixtureA.getBody().getUserData() + ", " + fixtureB.getBody().getUserData());
                if(fixtureA.getBody().getUserData().toString().contains("Map") &&
                        fixtureB.getBody().getUserData().toString().contains("Runner")) {
                    readyToJump();
                }

                if(fixtureA.getBody().getUserData().toString().contains("Runner") &&
                        fixtureB.getBody().getUserData().toString().contains("Mines")) {
                    if (!screenShaker.get_status()) {
                        screenShaker.shake(5, camera.position);
                    }
                    if (facing_left) {
                        runner.getBody().setLinearVelocity(7.5f, 10f);
                    }
                    else {
                        runner.getBody().setLinearVelocity(-7.5f, 10f);
                    }

                    is_jumping = true;
                    is_frozen = true;
                }
            }

            @Override
            public void endContact(Contact contact) {
                //Fixture fixtureA = contact.getFixtureA();
                //Fixture fixtureB = contact.getFixtureB();
                //System.out.println("endContact" + "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });

	}

	@Override
	public void render(float delta) {
        //System.out.println(sprite.getX() + " | " + bodyEdgeScreen.getPosition().x);

        //System.out.println("Camera: " + camera.position.x + ", " + camera.position.y);
        //System.out.println("Background: " + sprite2.getX() + ", " + sprite2.getY());

        //camera.position.set(sprite.getX() + sprite.getWidth() / 2, 0, 0);
        camera.position.set(runner.getPosition().x , 0, 0);

        screenShaker.update();
        batch.setProjectionMatrix(camera.combined);
        camera.update();

        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);

        /*sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.getWidth()/2,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 );*/

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //sprite2.draw(batch);
        map.drawMap(batch);
        /*batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());*/
        runner.update(delta);
        batch.draw(runner.getRunner(), runner.getPosition().x, runner.getPosition().y, runner.getWidth(), runner.getHeight());
        //System.out.println(runner.getWidth());

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
            runner.getBody().setLinearVelocity(-7.5f, runner.getBody().getLinearVelocity().y);
            runner.moveLeft();
        }
        else if (moving_right && !is_frozen) {
            runner.getBody().setLinearVelocity(7.5f, runner.getBody().getLinearVelocity().y);
            runner.moveRight();
        }

	}

    public void readyToJump() {
        if (!moving_left && !moving_right) {
            runner.getBody().setLinearVelocity(0f, 0f);
        }
        else {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 0f);
        }
        is_jumping = false;
        is_frozen = false;
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
	    this.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	    batch.dispose();
		img.dispose();
		world.dispose();
        runner.dispose();
	}

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            game.changeScreen(IronCurtain.MAINMENU);
        }
        return true;
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
                    runner.getBody().setLinearVelocity(0f, runner.getBody().getLinearVelocity().y);
                }
                facing_left = true;
                moving_left = true;
                left_pointer = pointer;
            } else if (screenX >= right_key.x && screenX <= (right_key.x + right_key.width) //right
                    && (Gdx.graphics.getHeight() - screenY) >= right_key.y && (Gdx.graphics.getHeight() - screenY) <= (right_key.y + right_key.height)) {
                if (moving_left) {
                    runner.getBody().setLinearVelocity(0f, runner.getBody().getLinearVelocity().y);
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
            runner.keyUpLeft();
        }

        if (moving_right && pointer == right_pointer) {
            moving_right = false;
            runner.keyUpRight();
        }

        if (!moving_left && !moving_right && is_jumping && !is_frozen) {
            runner.getBody().setLinearVelocity(0f, runner.getBody().getLinearVelocity().y);
        }

        if (!moving_left && !moving_right && !is_jumping && !is_frozen) {
            runner.getBody().setLinearVelocity(0f, 0f);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if ((old_screenY - screenY) > 256 && (old_screenX - screenX) < 256 && !is_jumping) {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 15f);
            is_jumping = true;
            runner.jump();
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
