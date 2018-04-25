package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lamoid.ironcurtain.IronCurtain;

public class SettingsScreen implements Screen{
    private IronCurtain game;
    private Stage stage;

    private SpriteBatch batch;

    private Texture bg_texture, settings_texture, back_texture;
    private Sprite bg_sprite;

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

        bg_texture = new Texture("settingsBG.png");
        bg_sprite = new Sprite(bg_texture);
        bg_sprite.setSize(bg_sprite.getWidth() / bg_sprite.getHeight()
                * IronCurtain.screenHeight, IronCurtain.screenHeight);
        bg_sprite.setPosition((IronCurtain.screenWidth - bg_sprite.getWidth())/2,0);

        Table table = new Table();
        table.setSize(IronCurtain.screenWidth * 0.6f, IronCurtain.screenHeight * 0.6f);
        table.setPosition((IronCurtain.screenWidth - table.getWidth()) * 0.5f,
                (IronCurtain.screenHeight - table.getHeight()) * 0.5f);
        stage.addActor(table);

        settings_texture = new Texture("settings.png");
        Drawable settings_drawable = new TextureRegionDrawable(new TextureRegion(settings_texture));
        Image settingsImage = new Image(settings_drawable);

        final SelectBox<String> languageSelectBox = new SelectBox<String>(game.skin);
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
        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundsEnabled());
        soundEffectsCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundsEnabled(enabled);
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
                if(!enabled) game.mainMenuBackgroundMusic.stop();
                else game.mainMenuBackgroundMusic.play();
                return false;
            }
        });

        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, game.skin);
        volumeMusicSlider.setValue(game.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume(volumeMusicSlider.getValue() );
                game.mainMenuBackgroundMusic.setVolume(game.getPreferences().getMusicVolume());
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

        final Label titleLabel = new Label(game.stringsBundle.get("settings"), game.skin, "verybig");
        final Label languageLabel = new Label(game.stringsBundle.get("language"), game.skin, "big");
        final Label soundOnOffLabel = new Label(game.stringsBundle.get("sounds"), game.skin, "big");
        final Label volumeSoundLabel = new Label(game.stringsBundle.get("sound_volume"), game.skin, "big");
        final Label musicOnOffLabel = new Label(game.stringsBundle.get("music"), game.skin, "big");
        final Label volumeMusicLabel = new Label(game.stringsBundle.get("music_volume"), game.skin, "big");
        final Label screenShakerOnOffLabel = new Label(game.stringsBundle.get("screen_shaker"), game.skin, "big");

        table.add(settingsImage).padBottom(20).align(Align.right);
        table.add(titleLabel).colspan(4).spaceLeft(100).align(Align.left);
        table.row().pad(20,0,20,0);
        table.add(languageLabel).pad(0,0,0,10).align(Align.left);
        table.add(languageSelectBox);
        table.add(screenShakerOnOffLabel).pad(0,50,0,10);
        table.add(screenShakerCheckbox);
        table.row().pad(20,0,20,0);
        table.add(volumeSoundLabel).pad(0,0,0,10).align(Align.left);
        table.add(soundMusicSlider).expandX().fillX();
        table.add(soundOnOffLabel).pad(0,50,0,10).align(Align.left);
        table.add(soundEffectsCheckbox);
        table.row().pad(20,0,20,0);
        table.add(volumeMusicLabel).pad(0,0,0,10).align(Align.left);
        table.add(volumeMusicSlider).expandX().fillX();
        table.add(musicOnOffLabel).pad(0,50,0,10).align(Align.left);
        table.add(musicCheckbox);

        back_texture = new Texture("settings_back.png");
        Drawable back_drawable = new TextureRegionDrawable(new TextureRegion(back_texture));
        ImageButton backButton = new ImageButton(back_drawable);
        backButton.setX(IronCurtain.screenWidth * 0.02f);
        backButton.setY(backButton.getX());
        backButton.setSize(IronCurtain.screenWidth / IronCurtain.screenHeight * backButton.getWidth() * 0.3f,
                IronCurtain.screenWidth / IronCurtain.screenHeight * backButton.getHeight() * 0.3f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(IronCurtain.MAINMENU, 0);
            }
        });
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        bg_sprite.draw(batch);
        if(languageChanged) {
            game.font.draw(batch, game.stringsBundle.get("languageChanged"), IronCurtain.screenWidth * 0.3f,
                    IronCurtain.screenHeight * 0.2f);

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
        bg_texture.dispose();
        settings_texture.dispose();
        back_texture.dispose();
    }
}
