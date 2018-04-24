package com.lamoid.ironcurtain.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.lamoid.ironcurtain.IronCurtain;
import com.lamoid.ironcurtain.sprites.*;
import com.lamoid.ironcurtain.utils.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class GameScreen implements Screen, InputProcessor {
  	private final IronCurtain game;
    private Stage stage;

    private SpriteBatch batch;
    private World world;
    private OrthographicCamera camera;
    private Rectangle moving_key, jump_key, health_bar, timer_bar;
    private ShapeRenderer shapeRenderer;
    private Sound lights;
    private Sound barking;
    private Sound mine_explosion;
    private Sound missile_launch;
    private Body explodedMine;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font256;
    private GlyphLayout startDelayLayout;
    private Random rand;

    private Runner runner;
    private Dogs dog;
    private Missile missile;
    private ScreenShaker screenShaker;
    private Map map;

    private List<Mines> mines;
    private List<Towers> towers;
    private List<Explosions> explosions;

    private boolean moving_left = false;
    private boolean moving_right = false;
    private boolean is_jumping = true;
    private boolean is_frozen = true;
    private boolean facing_left = false;
    boolean move_dog = false;
    boolean dog_attacked = false;
    boolean shoot_missile = false;
    boolean missile_direction = false;

    private float missile_timer = 0;
    private float attack_delay = 0;

    private float missile_vel_y = 0;
    private boolean missile_vel_saved = false;

    private float old_cameraX = 0;

    private int mine_count = 15;
    private int tower_count = 8;

    private int missile_delay = 0;
    private int dog_delay = 0;

    private int explosion_to_remove = -1;

    private float timeLimit = 60f;
    private float startDelay = 4.5f;

    private float volume = 0;

    private String str_startDelay;
    float fontX, fontY;

    private List<Float> timers;

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
        explosions = new ArrayList<Explosions>();
        lights = Gdx.audio.newSound(Gdx.files.internal("lights.mp3"));
        barking = Gdx.audio.newSound(Gdx.files.internal("dog.mp3"));
        mine_explosion = Gdx.audio.newSound(Gdx.files.internal("mine.mp3"));
        missile_launch = Gdx.audio.newSound(Gdx.files.internal("missile_launch.mp3"));
        //missile_impact = Gdx.audio.newSound(Gdx.files.internal("missile_impact.mp3"));

        if (game.getPreferences().isSoundEffectsEnabled()) {
            volume = game.getPreferences().getSoundVolume();
        }

        float x1 = map.getSprite().getX() + IronCurtain.screenWidth;
        float x2 = map.getLength() - IronCurtain.screenWidth;
        float y = (IronCurtain.screenHeight / 2) * -1;

        for (int i = 1; i < tower_count + 1; i++) {
            towers.add(new Towers((x2 - x1) / tower_count * i,
                    (x2 - x1) / tower_count * (i + 1), y));
            timers.add(new Float(0));
        }

        mines = new ArrayList<Mines>();

        for (int i = 1; i < mine_count + 1; i++) {
            mines.add(new Mines(world, (x2 - x1) / mine_count * i,
                    (x2 - x1) / mine_count * (i + 1), y));
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

        /*timer_bar = new Rectangle();
        timer_bar.x = moving_key.x;
        timer_bar.y = IronCurtain.screenHeight * 0.9f;
        timer_bar.width = health_bar.width;
        timer_bar.height = health_bar.height;*/

        shapeRenderer = new ShapeRenderer();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("BebasNeue.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 256;
        font256 = generator.generateFont(parameter);
        generator.dispose();

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));

        camera.update();

        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
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

                    if (!dog_attacked) {
                        runner.decreaseHealth(1f);
                    }

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

                    explodedMine = fixtureB.getBody();

                    mine_explosion.play(volume);
                    runner.decreaseHealth(1f);

                    is_jumping = true;
                    is_frozen = true;
                }

                if(fixtureA.getBody().getUserData().toString().contains("Missile") ||
                        fixtureB.getBody().getUserData().toString().contains("Missile")) {
                    System.out.println("beginContact" + "between " + fixtureA.toString() + " and " + fixtureB.toString());
                    System.out.println(fixtureA.getBody().getUserData() + ", " + fixtureB.getBody().getUserData());
                    if(fixtureA.getBody().getUserData().toString().contains("Runner") ||
                            fixtureB.getBody().getUserData().toString().contains("Runner")) {
                        if (!screenShaker.get_status()) {
                            screenShaker.shake(5, camera.position);
                        }

                        runner.decreaseHealth(1f);
                    }

                    missile.getBody().setLinearVelocity(0f, 0f);
                    //destroy body
                    shoot_missile = false;
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

        if (startDelay > 0f) {
            startDelay -= Gdx.graphics.getDeltaTime();
        }
        if (startDelay > 3f) {
            setStartDelayText("3");
        }
        else if (startDelay > 2f) {
            setStartDelayText("2");
        }
        else if (startDelay > 1f) {
            setStartDelayText("1");
        }
        else {
            if (startDelay > 0f) {
                setStartDelayText("GO");
                is_frozen = false;
            }
            timeLimit -= Gdx.graphics.getDeltaTime();
        }

        handleInput();

        //timer_bar.width = (IronCurtain.screenWidth * 0.9f) * (timeLimit * (100f / 35f)) / 100f;

        runner.setHealth(runner.getMaxHealth() * ((timeLimit * (100f / 60f)) / 100f));
        //System.out.println(timeLimit);

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

        if (dog != null && !move_dog) {
            world.destroyBody(dog.getBody());
            dog = null;
        }

        if (missile != null && !shoot_missile) {
            mine_explosion.play(volume);
            explosions.add(new Explosions(missile.getPosition()));
            world.destroyBody(missile.getBody());
            missile = null;
        }

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
        batch.draw(runner.getRunner(is_jumping), runner.getPosition().x, runner.getPosition().y,
                runner.getWidth() / runner.getHeight() * (IronCurtain.screenHeight / 5.7f),
                IronCurtain.screenHeight / 5.7f);
        //System.out.println(runner.getWidth());

        if (move_dog && attack_delay > dog_delay) {
            if (dog == null) {
                dog = new Dogs(world, camera);
                System.out.println(dog.getPosition());
            }

            dog.getBody().setLinearVelocity(-6f, 0f);
            dog.moveLeft();

            //dog.getBody().setLinearVelocity(-5f, 0f);


            if (dog.getBody().getPosition().x < camera.position.x / 100f - (IronCurtain.screenWidth * 1.1f) / 200f || dog_attacked) {
                move_dog = false;
                dog_attacked = false;
                dog.getBody().setLinearVelocity(0f, 0f);
                //barking.stop();
                //dog.set_sound_played(false);
                //destroy body
            }

            if (dog.getBody().getPosition().x > camera.position.x / 100f + IronCurtain.screenWidth / 200f
                    && !dog.get_sound_played()) {
                barking.play(volume);
                dog.set_sound_played(true);
            }

            dog.update(delta);
            batch.draw(dog.getDog(), dog.getPosition().x, dog.getPosition().y,
                    dog.getWidth(), dog.getHeight());
        }

        for (Mines mine : mines) {
            if (explodedMine != null && explodedMine == mine.getBody()) {
                explosions.add(new Explosions(mine.getPosition()));
                world.destroyBody(explodedMine);
                mine.setMineExploded();
                explodedMine = null;
            }

            if (!mine.getMineExploded()) {
                mine.update(delta);
                batch.draw(mine.getMine(), mine.getPosition().x, mine.getPosition().y, mine.getWidth(), mine.getHeight());
            }
        }

        drawExplosion(delta);

        if (shoot_missile) {
            if (attack_delay > missile_delay && missile == null) {
                missile = new Missile(world);
                missile.setPos(camera);
                missile_launch.play(volume * 0.5f);
            }
            else if (attack_delay > 100 && missile != null) {
                if (missile_timer < 50) {
                    missile.getBody().setLinearVelocity((runner.getBody().getPosition().x - missile.getBody().getPosition().x) * 1.25f,
                            ((IronCurtain.screenHeight * -0.4f) / 200f - missile.getBody().getPosition().y));
                }
                /*else if (missile_timer < 100) {
                    if (!missile_vel_saved) {
                        missile_vel_y = missile.getBody().getLinearVelocity().y;
                        missile_vel_saved = true;
                    }
                    missile_vel_y += 0.025f;
                    missile.getBody().setLinearVelocity(missile.getBody().getLinearVelocity().x, missile_vel_y);

                }*/
                else {
                    if (!missile_vel_saved) {
                        missile_vel_y = missile.getBody().getLinearVelocity().y;
                        missile_vel_saved = true;
                    }
                    //missile_vel_y -= 0.02f;
                    missile.getBody().setLinearVelocity(missile.getBody().getLinearVelocity().x, missile_vel_y);
                    //runner.getBody().getPosition().x - missile.getBody().getPosition().x) * 1.5f
                }

                missile.getBody().setTransform(missile.getBody().getWorldCenter(), missile.getBody().getLinearVelocity().angle());

                missile.updatePos();
                missile.drawMissile(batch);

                missile_timer++;
            }
        }

        if (startDelay > 0f) {
            font256.draw(batch, startDelayLayout, fontX, fontY);
        }

        batch.end();

        if (move_dog || shoot_missile) {
            attack_delay++;
        }

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

        //timer_bar.width = (IronCurtain.screenWidth * 0.9f) * (timeLimit * (100f / 35f)) / 100f;

        //shapeRenderer.setColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
        //shapeRenderer.rect(timer_bar.x, timer_bar.y, timer_bar.width, timer_bar.height);

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

        stage.act(delta);
        stage.draw();

        if (runner.getHealth() <= 0 || timeLimit <= 0 || runner.getPosition().x >= map.getSprite().getX() + map.getLength()) {
            game.changeScreen(IronCurtain.ENDGAME, runner.getHealth());
        }
	}

    public void readyToJump() {
        /*if (!moving_left && !moving_right || is_frozen) {
            runner.getBody().setLinearVelocity(0f, 0f);
        }
        else {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 0f);
        }*/
        if (facing_left) {
            runner.keyUpLeft();
        }
        else {
            runner.keyUpRight();
        }
        is_jumping = false;

        if (startDelay <= 0) {
            is_frozen = false;
        }
    }

    public void drawTowerSpotlights() {
        for (Towers tower : towers) {
            if (timers.get(towers.indexOf(tower)) < tower.getT()) {
                tower.drawSpotlight(shapeRenderer, camera);
                List<Float> coordinates;
                coordinates = tower.getCoordinates();

                boolean runner_check1 = false;
                boolean runner_check2 = false;
                boolean camera_check1 = false;
                boolean camera_check2 = false;

                //System.out.print("runner.x: " + runner.getPosition().x + Gdx.graphics.getWidth() / 2);

                for (Float coordinate : coordinates) {
                    if (coordinates.indexOf(coordinate) == 0) {
                        //System.out.print(" | tower" + towers.indexOf(tower) + ": " + (coordinate + camera.position.x));
                        if (runner.getPosition().x + IronCurtain.screenWidth/2
                                <= coordinate + camera.position.x) {
                            runner_check1 = true;
                        }
                        if (camera.position.x <= coordinate + camera.position.x
                                && timers.get(towers.indexOf(tower)) <= 0.1f) {
                            camera_check1 = true;
                            //System.out.println("check1 = " + camera_check1);
                        }
                    }

                    if (coordinates.indexOf(coordinate) == 1) {
                        //System.out.println(", " + (coordinate + camera.position.x));
                        if (runner.getPosition().x + runner.getWidth() + IronCurtain.screenWidth/2
                                >= coordinate + camera.position.x) {
                            runner_check2 = true;
                        }
                        //System.out.println("camera.position.x: " + (camera.position.x + IronCurtain.screenWidth/2)
                        // + "| coordinate: " + (coordinate));
                        if (camera.position.x + IronCurtain.screenWidth >= coordinate + camera.position.x
                                && timers.get(towers.indexOf(tower)) <= 0.1f) {
                            camera_check2 = true;
                            //System.out.println("check2 = " + camera_check2);
                        }
                    }

                    if (runner_check1 && runner_check2) {
                        //runner.setHealth(runner.getHealth() - 0.1f);

                        if (!move_dog || !shoot_missile) {
                            if (!screenShaker.get_status()) {
                                screenShaker.shake(5, camera.position);
                            }
                            rand = new Random();
                            attack_delay = 0;
                        }

                        if (!move_dog) {
                            //dog.resetPos(camera);
                            move_dog = true;
                            dog_delay = rand.nextInt((200 - 50) + 1) + 50;
                        }

                        if (!shoot_missile) {
                            missile_vel_saved = false;
                            missile_timer = 0;
                            missile_direction = facing_left;
                            shoot_missile = true;
                            missile_delay = rand.nextInt((200 - 50) + 1) + 50;
                        }

                        //time_to_shoot = true;
                    }

                    if (camera_check1 && camera_check2 && !tower.get_sound_played()) {
                        lights.play(volume * 0.5f);
                        tower.set_sound_played(true);
                    }
                }
            }
            else if (timers.get(towers.indexOf(tower)) >= tower.getT() * 2) {
                timers.set(towers.indexOf(tower), 0f);
                tower.set_sound_played(false);
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

    public void drawExplosion(float dt) {
        for (Explosions explosion : explosions) {
            if (!explosion.getAnimationFinished()) {
                explosion.update(dt);
                //System.out.println(explosion.getPosition());
                batch.draw(explosion.getExplosion(), explosion.getPosition().x - explosion.getWidth() / 2, explosion.getPosition().y,
                        explosion.getWidth(), explosion.getHeight());
            }
            else {
                explosion_to_remove = explosions.indexOf(explosion);
            }
        }
        if (explosion_to_remove != -1) {
            explosions.remove(explosion_to_remove);
            explosion_to_remove = -1;
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
                        game.changeScreen(IronCurtain.MAINMENU, 0);
                }
            }.show(stage);
        }
        return true;
    }

    void setStartDelayText(String str) {
        str_startDelay = str;
        startDelayLayout = new GlyphLayout(font256, str_startDelay);
        fontX = camera.position.x - startDelayLayout.width / 2;
        fontY = camera.position.y + startDelayLayout.height / 2;
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
                    runner.getBody().setLinearVelocity(-10f, runner.getBody().getLinearVelocity().y);
                    runner.moveLeft();
                    facing_left = true;
                    moving_left = true;
                    moving_right = false;
                }
                else if (touchX >= moving_key.x + moving_key.width/2 && touchX <= moving_key.x + moving_key.width) //right
                {
                    runner.getBody().setLinearVelocity(10f, runner.getBody().getLinearVelocity().y);
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
        if (screenX >= jump_key.x && screenX <= (jump_key.x + jump_key.width)
                && (IronCurtain.screenHeight - screenY) >= jump_key.y && (IronCurtain.screenHeight - screenY) <= (jump_key.y + jump_key.height)
                && !is_jumping) {
            runner.getBody().setLinearVelocity(runner.getBody().getLinearVelocity().x, 12.5f);
            is_jumping = true;
            //runner.jump();
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
