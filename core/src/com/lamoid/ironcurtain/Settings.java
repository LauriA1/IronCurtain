package com.lamoid.ironcurtain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    private static final String PREF_LANGUAGE = "language";
    private static final String PREF_SOUND_ENABLED = "soundEnabled";
    private static final String PREF_SOUND_VOLUME = "soundVolume";
    private static final String PREF_MUSIC_ENABLED = "musicEnabled";
    private static final String PREF_MUSIC_VOLUME = "musicVolume";
    private static final String PREF_SCREENSHAKER_ENABLED = "screenShakerEnabled";
    private static final String PREFS_NAME = "gameSettings";

    private Preferences preferences;

    protected Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public void changeLanguage(String language) {
        getPrefs().putString(PREF_LANGUAGE, language);
        getPrefs().flush();
    }

    public String getLanguage() {
        return getPrefs().getString(PREF_LANGUAGE, "English");
    }

    public boolean isSoundEffectsEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME, 0.5f);
    }

    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public boolean isScreenShakerEnabled() {
        return getPrefs().getBoolean(PREF_SCREENSHAKER_ENABLED, true);
    }

    public void setScreenShakerEnabled(boolean screenShakerEnabled) {
        getPrefs().putBoolean(PREF_SCREENSHAKER_ENABLED, screenShakerEnabled);
        getPrefs().flush();
    }
}
