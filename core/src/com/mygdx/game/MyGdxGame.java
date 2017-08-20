package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyGdxGame extends ApplicationAdapter {
    private volatile boolean lock;
    private SpriteBatch batch;
    private Background background;
    private Hero hero;
    private int maxScore;
    private final int ASTEROIDS_COUNT = 30;
    private ArrayList<Asteroid> asteroids;
    private ListIterator<Asteroid> itAsteroid;
    private float deltaTime;
    private ExecutorService updateThread;
    private BitmapFont scoreBox;

    @Override
    public void create() {
        lock = false;
        updateThread = Executors.newSingleThreadExecutor();
        batch = new SpriteBatch();
        scoreBox = new BitmapFont();
        background = new Background(batch);
        hero = new Hero(batch);

        asteroids = new ArrayList<>(ASTEROIDS_COUNT);
        for (int i = 0; i < ASTEROIDS_COUNT; i++)
            asteroids.add(new Asteroid(batch));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background.render();
        for (Asteroid asteroid : asteroids)
            asteroid.render();

        hero.render();

        scoreBox.draw(batch, "Score " + hero.getScore() + "\nMax " +
                        (maxScore > hero.getScore() ? maxScore : (maxScore = hero.getScore())),
                20, GameObject.height - 20);
        batch.end();

        update();
    }

    private void update() {
        if (!lock) {
            lock = true;
            updateThread.execute(this::check);
        }
    }

    private void check() {
        deltaTime = Gdx.graphics.getDeltaTime();
        background.update(deltaTime);

        itAsteroid = asteroids.listIterator();
        while (itAsteroid.hasNext()) {
            Asteroid asteroid = itAsteroid.next();

            if (heroCheck(asteroid))
                break;
            hero.asteroidCheck(asteroid);
        }

        hero.update(deltaTime);

        for (Asteroid asteroid : asteroids)
            asteroid.update(deltaTime);

        lock = false;
    }

    private boolean heroCheck(Asteroid asteroid) {
        if ((Math.abs(asteroid.getPosition().x - hero.getPosition().x) < (40 * asteroid.getScale())
                && Math.abs(asteroid.getPosition().y - hero.getPosition().y) < (30 * asteroid.getScale()))) {
            hero.reCreate();
            for (Asteroid asteroidForReCreate : asteroids)
                asteroidForReCreate.reCreate();

            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        hero.dispose();

        for (Asteroid asteroid : asteroids)
            asteroid.dispose();
    }
}