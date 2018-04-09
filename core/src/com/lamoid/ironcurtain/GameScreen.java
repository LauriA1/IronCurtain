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
//import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.lamoid.ironcurtain.sprites.Map;
import com.lamoid.ironcurtain.sprites.Runner;
import com.lamoid.ironcurtain.sprites.Towers;
import com.lamoid.ironcurtain.sprites.Dogs;

public class GameScreen implements Screen, InputProcessor {
  	final IronCurtain game;

    private SpriteBatch batch;
    private World world;
    private OrthographicCamera camera;
    private Rectangle left_key;
    private Rectangle right_key;
    private Rectangle jump_key;
    private Rectangle health_bar;
    private ShapeRenderer shapeRenderer;

    private Runner runner;
    private Dogs dog;
    private ScreenShaker screenShaker;
    private Map map;

    private List<Mines> mines;
    private List<Towers> towers;

    boolean moving_left = false;
    boolean moving_right = false;
    boolean is_jumping = true;
    boolean is_frozen = false;
    boolean facing_left = false;
    boolean time_to_shoot = false;
    boolean move_dog = false;
    boolean dog_attacked = false;

    float shoot_timer = 0;
    int tower_index = 0;

    int old_screenX = 0;
    int old_screenY = 0;
    int left_pointer = 0;
    int right_pointer = 0;

    float old_cameraX = 0;

    int mine_count = 15;
    int tower_count = 10;

    private List<Float> timers;

    float tower_distance = 512;
    float mine_distance = 512;

	public GameScreen(final IronCurtain gam) {
		this.game = gam;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());
        screenShaker = new ScreenShaker(camera, 32);
        world = new World(new Vector2(0, -20f),true);
        map = new Map(world);
        runner = new Runner(world, 0.32f, 0);
        timers = new ArrayList<Float>();
        towers = new ArrayList<Towers>();
        dog = new Dogs(world, camera);

        float x1 = map.getSprite().getX() + Gdx.graphics.getWidth();
        float x2 = map.getSprite().getX() + map.getSprite().getWidth() - Gdx.graphics.getWidth();
        float y = (Gdx.graphics.getHeight() / 2) * -1;

        for (int i = 0; i < tower_count; i++) {
            towers.add(new Towers((x2 - x1) / tower_count * i,
                    (x2 - x1) / tower_count * (i + 1), y));
            timers.add(new Float(0));
        }

        mines = new ArrayList<Mines>();

        for (int i = 0; i < mine_count; i++) {
            mines.add(new Mines(world, (x2 - x1) / tower_count * i,
                    (x2 - x1) / tower_count * (i + 1), y));
        }

        /*while (i < tower_count) {
            towers.add(new Towers(x1, x2, y));
            timers.add(new Float(0));
            int x = towers.get(i).getX();
            boolean is_overlapping = false;

            if (i > 0) {
                for (Towers tower : towers) {
                    if (Math.abs(tower.getX() - x) < tower_distance && towers.indexOf(tower) != i) {
                        is_overlapping = true;
                        //System.out.println("Overlapping mine #" + i);
                    }
                }

                if (is_overlapping) {
                    towers.remove(i);
                }

                if (towers.size() > i) {
                    i++;
                }
            }
            else {
                i++;
            }
        }*/

        left_key = new Rectangle();
        left_key.x = Gdx.graphics.getWidth() * 0.05f;
        left_key.y = left_key.x;
        left_key.width = left_key.x * 2;
        left_key.height = left_key.width;

        right_key = new Rectangle();
        right_key.x = left_key.x + left_key.width * 1.15f; //Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.15f);
        right_key.y = left_key.y;
        right_key.width = left_key.width;
        right_key.height = left_key.height;

        jump_key = new Rectangle();
        jump_key.x = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.15f);
        jump_key.y = left_key.y;
        jump_key.width = left_key.width;
        jump_key.height = left_key.height;

        health_bar = new Rectangle();
        health_bar.x = left_key.x;
        health_bar.y = Gdx.graphics.getHeight() * 0.95f;
        health_bar.width = (Gdx.graphics.getWidth() * 0.9f) * (runner.getHealth() / 10f);
        health_bar.height = left_key.height / 10;

        shapeRenderer = new ShapeRenderer();

        /*texture = new Texture(Gdx.files.internal("background.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        sprite2 = new Sprite(texture);
        sprite2.setOrigin(0,0);
        sprite2.setScale(3);
        sprite2.setPosition(-(Gdx.graphics.getWidth() / 2),-((Gdx.graphics.getHeight() / 2) + 32));*/

        /*i = 0;

        while (i < mine_count) {
            mines.add(new Mines(world, x1, x2, y));
            int x = mines.get(i).getX();
            boolean is_overlapping = false;

            if (i > 0) {
                for (Mines mine : mines) {
                    if (Math.abs(mine.getX() - x) < mine_distance && mines.indexOf(mine) != i) {
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
        }*/

        camera.update();

        Gdx.input.setInputProcessor(this);

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
                        fixtureB.getBody().getUserData().toString().contains("Dogs")) {
                    if (!screenShaker.get_status()) {
                        screenShaker.shake(5, camera.position);
                    }
                    /*if (facing_left) {
                        runner.getBody().setLinearVelocity(7.5f, 10f);
                    }
                    else {
                        runner.getBody().setLinearVelocity(-7.5f, 10f);
                    }*/

                    runner.setHealth(runner.getHealth() - 1f);

                    dog_attacked = true;

                    //is_jumping = true;
                    //is_frozen = true;
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

                    runner.setHealth(runner.getHealth() - 1f);

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

        if (runner.getHealth() <= 0) {
            game.setScreen(new GameScreen(game));
        }

        for (int i = 0; i < tower_count; i++) {
            timers.set(i, timers.get(i) + delta);
        }

        old_cameraX = camera.position.x;
        //System.out.println(runner.getPosition().x + Gdx.graphics.getWidth() / 2);
        if (runner.getPosition().x >= 32 && runner.getPosition().x <= map.getLength() - Gdx.graphics.getWidth()) {
            camera.position.set(runner.getPosition().x , 0, 0);
        }

        map.animateBackground(camera, old_cameraX);

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

        for (Towers tower : towers) {
            tower.drawTower(batch);
        }

        runner.update(delta);
        batch.draw(runner.getRunner(is_jumping), runner.getPosition().x, runner.getPosition().y, runner.getWidth(), runner.getHeight());
        //System.out.println(runner.getWidth());

        if (move_dog) {
            dog.getBody().setLinearVelocity(10f, 0f);

            if (dog.getBody().getPosition().x > camera.position.x / 100f + (Gdx.graphics.getWidth() * 1.1f) / 200f || dog_attacked) {
                move_dog = false;
                dog_attacked = false;
                dog.getBody().setLinearVelocity(0f, 0f);
                dog.resetPos(camera);
            }
        }

        dog.updatePos();
        dog.drawDog(batch);

        for (Mines mine : mines) {
            mine.drawMine(batch);
        }

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        drawTowerSpotlights();

        shapeRenderer.setColor(80 / 255.0f, 80 / 255.0f, 50 / 255.0f, 0.5f);
        shapeRenderer.rect(left_key.x, left_key.y, left_key.width, left_key.height);
        shapeRenderer.rect(right_key.x, right_key.y, right_key.width, right_key.height);
        shapeRenderer.rect(jump_key.x, jump_key.y, jump_key.width, jump_key.height);

        health_bar.width = (Gdx.graphics.getWidth() * 0.9f) * (runner.getHealth() / 10f);

        shapeRenderer.setColor(255 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
        shapeRenderer.rect(health_bar.x, health_bar.y, health_bar.width, health_bar.height);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

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
        if (!moving_left && !moving_right || is_frozen) {
            runner.getBody().setLinearVelocity(0f, 0f);
        }
        else {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 0f);
        }
        if (facing_left) {
            runner.keyUpLeft();
        }
        else {
            runner.keyUpRight();
        }
        is_jumping = false;
        is_frozen = false;
    }

    public void drawTowerSpotlights() {
        for (Towers tower : towers) {
            if (timers.get(towers.indexOf(tower)) < tower.getT()) {
                tower.drawSpotlight(shapeRenderer, camera);
                List<Float> coordinates;
                coordinates = tower.getCoordinates();

                boolean check1 = false;
                boolean check2 = false;

                //System.out.print("runner.x: " + runner.getPosition().x + Gdx.graphics.getWidth() / 2);

                for (Float coordinate : coordinates) {
                    if (coordinates.indexOf(coordinate) == 0) {
                        //System.out.print(" | tower" + towers.indexOf(tower) + ": " + (coordinate + camera.position.x));
                        if (runner.getPosition().x + runner.getWidth() + Gdx.graphics.getWidth() / 2
                                <= coordinate + camera.position.x) {
                            check1 = true;
                        }
                    }

                    if (coordinates.indexOf(coordinate) == 1) {
                        //System.out.println(", " + (coordinate + camera.position.x));
                        if (runner.getPosition().x + Gdx.graphics.getWidth() / 2
                                >= (coordinate + camera.position.x)) {
                            check2 = true;
                        }
                    }

                    if (check1 && check2) {
                        screenShaker.shake(5, camera.position);
                        //runner.setHealth(runner.getHealth() - 0.1f);

                        if (!move_dog) {
                            dog.resetPos(camera);
                            move_dog = true;
                        }

                        //time_to_shoot = true;
                    }
                }
            }
            else if (timers.get(towers.indexOf(tower)) >= tower.getT() * 2) {
                timers.set(towers.indexOf(tower), 0f);
            }

            /*if (time_to_shoot) {
                shoot_timer += delta;
                if (shoot_timer >= 3f) {
                    screenShaker.shake(5, camera.position);
                    runner.setHealth(runner.getHealth() - 1f);
                    time_to_shoot = false;
                    shoot_timer = 0;
                }
                else if (shoot_timer >= 1f) {
                    tower.drawGunfire(shapeRenderer, camera);
                }
            }*/
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
		world.dispose();
		runner.dispose();
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
            } else if (screenX >= jump_key.x && screenX <= (jump_key.x + jump_key.width) //jump
                    && (Gdx.graphics.getHeight() - screenY) >= jump_key.y && (Gdx.graphics.getHeight() - screenY) <= (jump_key.y + jump_key.height)
                    && !is_jumping) {
                runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 15f);
                is_jumping = true;
                runner.jump();
            }
            /*else {
                old_screenX = screenX;
                old_screenY = screenY;
            }*/
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
        /*if ((old_screenY - screenY) > 256 && (old_screenX - screenX) < 256 && !is_jumping) {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 15f);
            is_jumping = true;
            runner.jump();
        }*/

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
