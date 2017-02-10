package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Vsevolod on 01/02/2017.
 */
public final class Background extends GameObject {
    private static final Texture backgroundTexture = new Texture("staticback.jpg");
    private static final Texture starTexture = new Texture("star12.tga");
    private static final int STARS_COUNT = 60;

    private final Star[] stars;

    private final class Star{
        private float velocity;
        private Vector2 position;

        public Star() {
            velocity = 6f + (float) Math.random() * 30;
            position = new Vector2((float) Math.random() * width,
                    (float) Math.random() * height);
        }

        public void render() {
            batch.draw(starTexture, position.x, position.y);
        }

        public void update(float deltaTime) {
            position = new Vector2(position.x - velocity * deltaTime, position.y);

            if (position.x < -20f) {
                reCreate();
            }
        }

        private void reCreate() {
            velocity = 6f + (float) Math.random() * 30f;
            position = new Vector2(width,
                    (float) Math.random() * height);
        }
    }

    public Background(SpriteBatch batch) {
        super(batch);
        stars = new Star[STARS_COUNT];

        for (int i = 0; i < stars.length; i++)
            stars[i] = new Star();
    }

    @Override
    public void render() {
        batch.draw(backgroundTexture, position.x, position.y);

        for (Star star : stars)
            star.render();
    }

    @Override
    public void update(float deltaTime) {
        for (Star star : stars)
            star.update(deltaTime);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        starTexture.dispose();
    }
}