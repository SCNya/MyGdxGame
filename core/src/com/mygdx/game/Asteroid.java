package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Vsevolod on 02/02/2017.
 */
public final class Asteroid extends GameObject {
    private static final Texture asteroidTexture = new Texture("asteroid64.png");
    private final Random random;
    private float yVelocity;
    private float angle;
    private float angleVelocity;
    private float scale;
    private int health;

    public Asteroid(SpriteBatch batch) {
        super(batch, 240f + (float) Math.random() * 200f, new Vector2((float) Math.random() * width  + width,
                (float) Math.random() * height));
        random = new Random();
        setAttributes();
    }

    private void setAttributes() {
        scale = 0.5f + (float) Math.random() * 1f;
        health = Math.round(scale * 2f);
        yVelocity = 30f - (float) random.nextInt(61);
        angle = (float) random.nextInt(361);
        angleVelocity = (60f - (float) random.nextInt(121));
    }

    public void hit(int dmg) {
        health -= dmg;
        scale *= 0.8f;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void render() {
        batch.draw(asteroidTexture, position.x, position.y,
                32, 32, 64, 64, scale, scale, angle,
                0, 0, 64, 64, false, false);
    }

    @Override
    public void update(float deltaTime) {
        position = new Vector2(position.x - velocity * deltaTime, position.y + yVelocity * deltaTime);

        if (position.x < -64f || position.y > height || position.y < -height) {
            reCreate();
        }

        angle += angleVelocity * deltaTime;

        if (angle > 360f)
            angle -= 360f;
        if (angle < 0f)
            angle += 360f;
    }

    public void reCreate() {
        velocity = 24f + (float) Math.random() * 600f;
        position = new Vector2((float) Math.random() * width  + width,
                (float) Math.random() * height);
        setAttributes();
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void dispose() {
        asteroidTexture.dispose();
    }
}
