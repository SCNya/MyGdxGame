package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vsevolod on 02/02/2017.
 */
public final class Hero extends GameObject {
    private static final Texture heroTexture = new Texture("ship80x60.tga");
    private static final Texture bulletTexture = new Texture("bullet32.png");
    private ReentrantLock lock;
    private List<Bullet> bullets;
    private Iterator<Bullet> itBullet;
    private final int fireRate;
    private int fireCounter;
    private int score;

    private class Bullet extends GameObject {

        public Bullet(SpriteBatch batch, Vector2 position) {
            super(batch, 500f, position);
        }

        @Override
        public void render() {
            batch.draw(bulletTexture, position.x + 40, position.y);
        }

        @Override
        public void update(float deltaTime) {
            position = new Vector2(position.x + velocity * deltaTime, position.y);
        }

        @Override
        public void dispose() {
            bulletTexture.dispose();
        }
    }

    public Hero(SpriteBatch batch) {
        super(batch, 200, new Vector2(100f, height / 2f));
        this.lock = new ReentrantLock();
        this.bullets = new LinkedList<>();
        fireCounter = 0;
        fireRate = 12;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    private void addScore(int point) {
        score += point;
    }

    @Override
    public void render() {
        batch.draw(heroTexture, position.x, position.y);

        sync(() -> {
            for (Bullet it : bullets)
                it.render();
        });
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            position = new Vector2(position.x,
                    position.y + velocity * deltaTime);

            if (position.y > height)
                position = new Vector2(position.x, -60f);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            position = new Vector2(position.x,
                    position.y - velocity * deltaTime);

            if (position.y < -60f)
                position = new Vector2(position.x, height);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {

            if (position.x <= width)
                position = new Vector2(position.x + velocity * deltaTime,
                        position.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {

            if (position.x > -10f)
                position = new Vector2(position.x - velocity * deltaTime,
                        position.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireCounter > fireRate) {
                fire();
                fireCounter = 0;
            } else
                fireCounter += 60 * deltaTime;
        }

        bulletUpdate(deltaTime);
    }

    public void asteroidCheck(Asteroid asteroid) {
        sync(() -> {
            itBullet = bullets.iterator();
            while (itBullet.hasNext()) {
                Bullet bullet = itBullet.next();
                if (Math.abs(asteroid.getPosition().x - bullet.getPosition().x) < (32 * asteroid.getScale())
                        && Math.abs(asteroid.getPosition().y - bullet.getPosition().y)
                        < (32 * asteroid.getScale())) {
                    itBullet.remove();
                    asteroid.hit(1);
                    if (asteroid.getHealth() < 1) {
                        addScore(1);
                        asteroid.reCreate();
                    }
                    break;
                }
            }
        });
    }

    private void bulletUpdate(float deltaTime) {
        sync(() -> {
            itBullet = bullets.iterator();
            while (itBullet.hasNext()) {
                Bullet bullet = itBullet.next();

                if (bullet.getPosition().x > width)
                    itBullet.remove();

                bullet.update(deltaTime);
            }
        });
    }

    private void fire() {
        sync(() -> bullets.add(new Bullet(batch, position)));
    }

    public void reCreate() {
        position = new Vector2(100f, height / 2f);
        score = 0;
        bulletReCreate();
    }

    private void bulletReCreate() {
        sync(() -> bullets = new LinkedList<>());
    }

    private void sync(Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void dispose() {
        heroTexture.dispose();
        for (Bullet bullet : bullets)
            bullet.dispose();
    }
}