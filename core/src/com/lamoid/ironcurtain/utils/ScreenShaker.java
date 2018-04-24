package com.lamoid.ironcurtain.utils;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class ScreenShaker {
    private static Random random = new Random();

    private boolean shaking;
    private int shakes;
    private int max_shakes;
    private Vector3 camPos;
    private float radius;
    private Camera camera;

    private boolean shaked = false;

    public ScreenShaker(Camera camera, float shakeRadius) {
        this.shaking = false;
        this.shakes = 0;
        this.shaked = false;
        this.max_shakes = 0;
        this.camPos = new Vector3();
        this.radius = shakeRadius;
        this.camera = camera;
    }

    public void update() {
        if (this.shaking) {
            float n1 = random.nextFloat() * this.radius
                    * (random.nextInt(2) == 0 ? 1 : -1);
            float n2 = random.nextFloat() * this.radius
                    * (random.nextInt(2) == 0 ? 1 : -1);

            if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
                Gdx.input.vibrate(10);
            }

            this.camera.translate(n2, n1, 0);

            if (this.shaked) {
                this.shaked = false;
                this.camera.position.set(this.camPos);
            } else {
                this.shaked = true;
            }

            this.shakes++;

            if (this.shakes > this.max_shakes) {
                this.shaking = false;
                this.max_shakes = 0;
                this.shakes = 0;
                this.camera.position.set(this.camPos);

                if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
                    Gdx.input.cancelVibrate();
                }
            }
        }
    }

    public void shake(int frames, Vector3 center) {
        this.shake(frames, center, this.radius);
    }

    public void shake(int frames, Vector3 center, float distance) {
        if (!this.shaking) {
            this.max_shakes = frames;
            this.shaking = true;
            this.camPos.set(center);
            this.radius = distance;
        }
    }

    public boolean get_status() {
        return shaking;
    }
}