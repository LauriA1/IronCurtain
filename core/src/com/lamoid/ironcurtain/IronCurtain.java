package com.lamoid.ironcurtain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.lamoid.ironcurtain.screens.*;
import java.util.Locale;

public class IronCurtain extends Game {

	public BitmapFont font;
    public Skin skin;

	private IntroScreen introScreen;
	private SettingsScreen settingsScreen;
	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	private EndScreen endScreen;
	private Settings settings;
    public Music mainMenuBackgroundMusic;

	public final static int MAINMENU = 0;
	public final static int SETTINGS = 1;
	public final static int THEGAME = 2;
	public final static int ENDGAME = 3;

    private Locale locale;
    private FileHandle baseFileHandle;
    public I18NBundle stringsBundle;

    public static float screenWidth;
    public static float screenHeight;

	public void create() {
        screenWidth = (float)Gdx.graphics.getWidth();
        screenHeight = (float)Gdx.graphics.getHeight();

		// Use LibGDX's default Arial font.
		font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        introScreen = new IntroScreen(this);
        settings = new Settings();

        mainMenuBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
        mainMenuBackgroundMusic.setLooping(true);
        mainMenuBackgroundMusic.setVolume(this.getPreferences().getMusicVolume());

        if(this.getPreferences().getLanguage().equals("Suomi")) {
            locale = new Locale("fi");
        } else {
            locale = Locale.ROOT;
        }
        baseFileHandle = Gdx.files.internal("i18n/stringsBundle");
        stringsBundle = I18NBundle.createBundle(baseFileHandle, locale);

        setScreen(introScreen);
	}

	public void changeScreen(int newScreen, float health){
        switch(newScreen){
            case MAINMENU:
                if(this.getPreferences().isMusicEnabled()){
                    this.setMusicPlay();
                }
                mainMenuScreen = new MainMenuScreen(this);
                this.setScreen(mainMenuScreen);
                break;
            case SETTINGS:
                settingsScreen = new SettingsScreen(this);
                this.setScreen(settingsScreen);
                break;
            case THEGAME:
                gameScreen = new GameScreen(this);
                this.setScreen(gameScreen);
                break;
            case ENDGAME:
                endScreen = new EndScreen(this, health);
                this.setScreen(endScreen);
                break;
        }
    }

    public Settings getPreferences() {
        return this.settings;
    }

    public void setMusicStop() {
        mainMenuBackgroundMusic.stop();
    }
    public void setMusicPlay() {
        mainMenuBackgroundMusic.play();
    }
    public void setMusicVolume() {
        mainMenuBackgroundMusic.setVolume(this.getPreferences().getMusicVolume());
    }
    public float getMusicVolume() {
        return mainMenuBackgroundMusic.getVolume();
    }

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		font.dispose();
	}

}
