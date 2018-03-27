package com.lamoid.ironcurtain.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;
    private boolean runnerDirection;

    public Animation(TextureRegion region, int frameCount, float cycleTime){
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / frameCount;
        for(int i = 0; i < frameCount; i++){
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
    }

    public void update(float dt){
        currentFrameTime += dt;
        if(currentFrameTime > maxFrameTime){
            frame++;
            currentFrameTime = 0;
        }
        if(frame >= frameCount)
            frame = 0;
    }
    // direction true == RIGHT, false == LEFT
    public void setDirection(float cycleT, boolean direction){
        maxFrameTime = cycleT / frameCount;
        runnerDirection = direction;
    }

    public TextureRegion getFrame(){
        //frames.get(frame).flip(direction,false);
        if(runnerDirection == false) {
            if (!frames.get(frame).isFlipX()) {
                frames.get(frame).flip(true, false);
            }
        }
        else if (runnerDirection == true) {
            if (frames.get(frame).isFlipX()) {
                frames.get(frame).flip(true, false);
            }
        }
        return frames.get(frame);
    }
}
