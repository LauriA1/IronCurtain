package com.lamoid.ironcurtain.screens;

import com.badlogic.gdx.Screen;
import com.lamoid.ironcurtain.IronCurtain;

public class IntroScreen implements Screen {

    private IronCurtain game;

    public IntroScreen(IronCurtain gam){
        game = gam;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.changeScreen(IronCurtain.MAINMENU);
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

    }

    @Override
    public void dispose() {

    }
}
