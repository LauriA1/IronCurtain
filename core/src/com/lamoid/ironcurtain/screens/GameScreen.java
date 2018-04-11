package com.lamoid.ironcurtain.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.sprites.*;
import com.lamoid.ironcurtain.utils.ScreenShaker;

public class GameScreen implements Screen, InputProcessor {
  	private final IronCurtain game;
    private Stage stage;

    private SpriteBatch batch;
    private World world;
    private OrthographicCamera camera;
    private Rectangle moving_key, jump_key, health_bar;
    private ShapeRenderer shapeRenderer;

    private Runner runner;
    private Dogs dog;
    private ScreenShaker screenShaker;
    private Map map;

    private List<Mines> mines;
    private List<Towers> towers;

    private boolean moving_left = false;
    private boolean moving_right = false;
    private boolean is_jumping = true;
    private boolean is_frozen = false;
    private boolean facing_left = false;
    private boolean time_to_shoot = false;
    boolean move_dog = false;
    boolean dog_attacked = false;

    private float shoot_timer = 0;
    private int tower_index = 0;

    private float old_cameraX = 0;

    private int mine_count = 8;
    private int tower_count = 5;

    private List<Float> timers;

    private float tower_distance = 512;
    private float mine_distance = 512;

	public GameScreen(final IronCurtain gam) {
		this.game = gam;
        stage = new Stage(new ScreenViewport());

        batch = new SpriteBatch();
        camera = new OrthographicCamera(IronCurtain.screenWidth, IronCurtain.screenHeight);
        screenShaker = new ScreenShaker(camera, 32);
        world = new World(new Vector2(0, -20f),true);
        map = new Map(world);
        runner = new Runner(world, 0.32f, 0);
        timers = new ArrayList<Float>();
        towers = new ArrayList<Towers>();
        dog = new Dogs(world, camera);

        float x1 = map.getSprite().getX() + IronCurtain.screenWidth;
        float x2 = map.getSprite().getX() + map.getSprite().getWidth() - IronCurtain.screenWidth;
        float y = (IronCurtain.screenHeight / 2) * -1;

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

        moving_key = new Rectangle();
        moving_key.x = IronCurtain.screenWidth * 0.05f;
        moving_key.y = moving_key.x;
        moving_key.width = moving_key.x * 4;
        moving_key.height = moving_key.width/2;

        jump_key = new Rectangle();
        jump_key.x = IronCurtain.screenWidth - (IronCurtain.screenWidth * 0.15f);
        jump_key.y = moving_key.y;
        jump_key.width = moving_key.width/2;
        jump_key.height = moving_key.height;

        health_bar = new Rectangle();
        health_bar.x = moving_key.x;
        health_bar.y = IronCurtain.screenHeight * 0.95f;
        health_bar.width = (IronCurtain.screenWidth * 0.9f) * (runner.getHealth() / 10f);
        health_bar.height = moving_key.height / 10;

        shapeRenderer = new ShapeRenderer();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));

        camera.update();

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

        handleInput();

        if (runner.getHealth() <= 0) {
            game.changeScreen(IronCurtain.THEGAME);
        }

        for (int i = 0; i < tower_count; i++) {
            timers.set(i, timers.get(i) + delta);
        }

        old_cameraX = camera.position.x;
        //System.out.println(runner.getPosition().x + Gdx.graphics.getWidth() / 2);
        if (runner.getPosition().x >= 32 && runner.getPosition().x <= map.getLength() - IronCurtain.screenWidth) {
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

            if (dog.getBody().getPosition().x > camera.position.x / 100f + (IronCurtain.screenWidth * 1.1f) / 200f || dog_attacked) {
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
        shapeRenderer.rect(moving_key.x, moving_key.y, moving_key.width, moving_key.height);
        shapeRenderer.rect(jump_key.x, jump_key.y, jump_key.width, jump_key.height);

        health_bar.width = (IronCurtain.screenWidth * 0.9f) * (runner.getHealth() / 10f);

        shapeRenderer.setColor(255 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
        shapeRenderer.rect(health_bar.x, health_bar.y, health_bar.width, health_bar.height);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.act(delta);
        stage.draw();
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

                //System.out.print("runner.x: " + (runner.getPosition().x - runner.getWidth()/2 + Gdx.graphics.getWidth()/2));

                for (Float coordinate : coordinates) {
                    if (coordinates.indexOf(coordinate) == 0) {
                        //System.out.print(" | tower" + towers.indexOf(tower) + ": " + (coordinate + camera.position.x));
                        if (runner.getPosition().x + IronCurtain.screenWidth/2
                                <= coordinate + camera.position.x) {
                            check1 = true;
                        }
                    }

                    if (coordinates.indexOf(coordinate) == 1) {
                        //System.out.println(", " + (coordinate + camera.position.x));
                        if (runner.getPosition().x + runner.getWidth()/2 + IronCurtain.screenWidth/2
                                >= coordinate + camera.position.x) {
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
	    stage.dispose();
		world.dispose();
        runner.dispose();
	}

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            //final Dialog endDialog =
            new Dialog("Confirm exit", game.skin) {
                {
                    final Label textLabel = new Label("Are you sure?",
                            game.skin, "black");
                    text(textLabel);

                    //text("rly exit");

                    /*final TextButton yes_button = new TextButton("Yes", game.skin);
                    final TextButton no_button = new TextButton("No", game.skin);
                    button(yes_button, "Yes");
                    button(no_button, "No");*/

                    button("Yes", "Yes");
                    button("No", "No");
                }
                @Override
                protected void result(final Object object)
                {
                    if(object.toString().equals("Yes"))
                        game.changeScreen(IronCurtain.MAINMENU);
                }
            }.show(stage);
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

    private void handleInput() {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();

        if(Gdx.input.isTouched()
                && touchX >= moving_key.x && touchX <= moving_key.x + moving_key.width
                && IronCurtain.screenHeight - touchY >= moving_key.y
                && IronCurtain.screenHeight - touchY <= moving_key.y + moving_key.height) {
            if (!is_frozen) {
                if (touchX >= moving_key.x && touchX <= (moving_key.x + moving_key.width/2)) //left
                {
                    runner.getBody().setLinearVelocity(-7.5f, runner.getBody().getLinearVelocity().y);
                    runner.moveLeft();
                    facing_left = true;
                    moving_left = true;
                    moving_right = false;
                }
                else if (touchX >= moving_key.x + moving_key.width/2 && touchX <= moving_key.x + moving_key.width) //right
                {
                    runner.getBody().setLinearVelocity(7.5f, runner.getBody().getLinearVelocity().y);
                    runner.moveRight();
                    facing_left = false;
                    moving_right = true;
                    moving_left = false;
                }
            }
        } else {
            if (moving_left) {
                moving_left = false;
                runner.keyUpLeft();
            }
            if (moving_right) {
                moving_right = false;
                runner.keyUpRight();
            }
            if (!moving_left && !moving_right && is_jumping && !is_frozen) {
                runner.getBody().setLinearVelocity(0f, runner.getBody().getLinearVelocity().y);
            }
            if (!moving_left && !moving_right && !is_jumping && !is_frozen) {
                runner.getBody().setLinearVelocity(0f, 0f);
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //System.out.println("touchDown registered!");
        if (screenX >= jump_key.x && screenX <= (jump_key.x + jump_key.width) //jump
                && (IronCurtain.screenHeight - screenY) >= jump_key.y && (IronCurtain.screenHeight - screenY) <= (jump_key.y + jump_key.height)
                && !is_jumping) {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 15f);
            is_jumping = true;
            runner.jump();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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