package com.lamoid.ironcurtain.state;

public abstract class State {

    protected GameStateManager gsm;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
    }
}
