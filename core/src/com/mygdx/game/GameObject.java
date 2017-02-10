package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Vsevolod on 02/02/2017.
 */
public abstract class GameObject {
    public static final int width = Gdx.graphics.getBackBufferWidth();
    public static final int height = Gdx.graphics.getBackBufferHeight();
    protected final SpriteBatch batch;
    protected float velocity;
    protected Vector2 position;

    public GameObject(SpriteBatch batch) {
        this(batch, 0f, new Vector2(0f, 0f));
    }

    public GameObject(SpriteBatch batch, float velocity, Vector2 position) {
        this.batch = batch;
        this.velocity = velocity;
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public abstract void render();

    public abstract void update(float deltaTime);

    public abstract void dispose();
}
