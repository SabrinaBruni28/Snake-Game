package io.github.SnakeGame.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import io.github.SnakeGame.Main;
import com.badlogic.gdx.graphics.GL20;
import io.github.SnakeGame.gameobjects.Snake;
import io.github.SnakeGame.screens.WinScreen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.SnakeGame.gameobjects.Direction;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.SnakeGame.screens.GameOverScreen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class AbstractGameScreen implements Screen {
    protected final Main game;

    protected Snake snake;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected ShapeRenderer shapeRenderer;

    protected float timer = 0;
    protected float touchStartX, touchStartY;

    protected boolean directionChanged = false;

    protected final float MOVE_INTERVAL = 0.13f;
    protected final float SWIPE_THRESHOLD = 50f;

    public AbstractGameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        snake = new Snake();
        initFood();

        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchStartX = screenX;
                touchStartY = screenY;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                float deltaX = screenX - touchStartX;
                float deltaY = screenY - touchStartY;

                if (!directionChanged) {
                    // Lógica de swipe
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                            if (deltaX > 0) {
                                snake.setDirection(Direction.RIGHT);
                                directionChanged = true;
                            } else {
                                snake.setDirection(Direction.LEFT);
                                directionChanged = true;
                            }
                        }
                    } 
                    else {
                        if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                            if (deltaY > 0) {
                                snake.setDirection(Direction.DOWN);
                                directionChanged = true;
                            } else {
                                snake.setDirection(Direction.UP);
                                directionChanged = true;
                            }
                        }
                    }
                }

                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            snake.draw(shapeRenderer);
            drawFood();
        shapeRenderer.end();

        batch.begin();
            drawUI();
        batch.end();
    }

    protected void update(float delta) {
        timer += delta;
        handleInput();

        if (timer >= MOVE_INTERVAL) {
            snake.move();

            directionChanged = false;

            if (snake.isCollidingWithSelf() || snake.isOutOfBounds()) {
                endGame(false);
                return;
            }

            handleFoodCollisions();

            timer = 0;
        }
    }

    protected void handleInput() {
        if (directionChanged) return;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            snake.setDirection(Direction.LEFT);
            directionChanged = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            snake.setDirection(Direction.UP);
            directionChanged = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            snake.setDirection(Direction.DOWN);
            directionChanged = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            snake.setDirection(Direction.RIGHT);
            directionChanged = true;
        }
    }

    protected void endGame(boolean won) {
        int score = snake.getLength() - 1;
        if (won) {
            game.setScreen(new WinScreen(game, score));
        } 
        else {
            game.setScreen(new GameOverScreen(game, score));
        }
    }

    protected abstract void initFood();
    protected abstract void drawFood();
    protected abstract void drawUI();
    protected abstract void handleFoodCollisions();

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
