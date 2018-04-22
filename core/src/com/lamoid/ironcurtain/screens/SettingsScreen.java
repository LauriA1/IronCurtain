package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class SettingsScreen implements Screen{
    private IronCurtain game;
    private Stage stage;

    private SpriteBatch batch;

    private Texture texture;
    private Sprite background_sprite;

    private boolean languageChanged = false;

    public SettingsScreen(IronCurtain gam){
        game = gam;

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        stage.clear();

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        texture = new Texture("background1.png");
        background_sprite = new Sprite(texture);
        background_sprite.setSize(background_sprite.getWidth() / background_sprite.getHeight()
                * IronCurtain.screenHeight, IronCurtain.screenHeight);
        background_sprite.setPosition((IronCurtain.screenWidth - background_sprite.getWidth())/2,0);

        final SelectBox<String> languageSelectBox = new SelectBox<String>(game.skin, "custom");
        languageSelectBox.setItems("English", "Suomi");
        languageSelectBox.setSelected(game.getPreferences().getLanguage());
        languageSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getPreferences().changeLanguage(languageSelectBox.getSelected());
                languageChanged = true;
            }
        });

        final CheckBox soundEffectsCheckbox = new CheckBox(null, game.skin);
        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundEffectsCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                return false;
            }
        });

        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f, false, game.skin);
        soundMusicSlider.setValue(game.getPreferences().getSoundVolume());
        soundMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume(soundMusicSlider.getValue() );
                return false;
            }
        });

        final CheckBox musicCheckbox = new CheckBox(null, game.skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusicEnabled(enabled);
                return false;
            }
        });

        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, game.skin);
        volumeMusicSlider.setValue(game.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume(volumeMusicSlider.getValue() );
                return false;
            }
        });

        final CheckBox screenShakerCheckbox = new CheckBox(null, game.skin);
        screenShakerCheckbox.setChecked(game.getPreferences().isScreenShakerEnabled());
        screenShakerCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = screenShakerCheckbox.isChecked();
                game.getPreferences().setScreenShakerEnabled(enabled);
                return false;
            }
        });

        final TextButton backButton = new TextButton(game.stringsBundle.get("back"), game.skin, "custom-small");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(IronCurtain.MAINMENU, 0);
            }
        });

        final Label titleLabel = new Label(game.stringsBundle.get("settings"), game.skin, "custom-bold");
        final Label languageLabel = new Label(game.stringsBundle.get("language"), game.skin, "custom");
        final Label soundOnOffLabel = new Label(game.stringsBundle.get("sounds"), game.skin, "custom");
        final Label volumeSoundLabel = new Label(game.stringsBundle.get("sound_volume"), game.skin, "custom");
        final Label musicOnOffLabel = new Label(game.stringsBundle.get("music"), game.skin, "custom");
        final Label volumeMusicLabel = new Label(game.stringsBundle.get("music_volume"), game.skin, "custom");
        final Label screenShakerOnOffLabel = new Label(game.stringsBundle.get("screen_shaker"), game.skin, "custom");

        table.add(titleLabel).colspan(2);
        table.row().pad(10,0,0,0);
        table.add(languageLabel);
        table.add(languageSelectBox);
        table.row();
        table.add(soundOnOffLabel);
        table.add(soundEffectsCheckbox);
        table.row();
        table.add(volumeSoundLabel);
        table.add(soundMusicSlider);
        table.row();
        table.add(musicOnOffLabel);
        table.add(musicCheckbox);
        table.row();
        table.add(volumeMusicLabel);
        table.add(volumeMusicSlider);
        table.row();
        table.add(screenShakerOnOffLabel);
        table.add(screenShakerCheckbox);
        table.row().pad(10,0,0,0);
        table.add(backButton).colspan(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!game.getPreferences().isMusicEnabled()){
            game.setMusicStop();
        }
        if(game.getPreferences().isMusicEnabled()){
            game.setMusicPlay();
        }
        if(game.getMusicVolume() != game.getPreferences().getMusicVolume()) {
            game.setMusicVolume();
        }

        batch.begin();
        background_sprite.draw(batch);
        if(languageChanged) {
            game.font.draw(batch, game.stringsBundle.get("languageChanged"), IronCurtain.screenWidth*0.2f,
                    IronCurtain.screenHeight*0.2f);

            Timer.schedule(new Task(){
                @Override
                public void run() {
                    languageChanged = false;
                }
            }, 5);
        }
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        texture.dispose();
    }
}
