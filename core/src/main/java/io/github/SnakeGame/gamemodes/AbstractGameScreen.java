package io.github.SnakeGame.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import io.github.SnakeGame.Main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.github.SnakeGame.gameobjects.Snake;
import io.github.SnakeGame.screens.WinScreen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.SnakeGame.gameobjects.Direction;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.SnakeGame.screens.GameOverScreen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.SnakeGame.GameConfig;

public abstract class AbstractGameScreen implements Screen {
    protected final Main game;

    protected Snake snake;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected ShapeRenderer shapeRenderer;

    protected Viewport viewport;
    protected OrthographicCamera camera;

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
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        
        font = new BitmapFont();
        font.getData().setScale(1.2f);

        snake = new Snake();
        initFood();
        initTime();

        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldTouch = viewport.unproject(new Vector2(screenX, screenY));
                touchStartX = worldTouch.x;
                touchStartY = worldTouch.y;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector2 worldTouch = viewport.unproject(new Vector2(screenX, screenY));
                float deltaX = worldTouch.x - touchStartX;
                float deltaY = worldTouch.y - touchStartY;

                if (!directionChanged) {
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
                    } else {
                        if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                            if (deltaY > 0) {
                                snake.setDirection(Direction.UP);
                                directionChanged = true;
                            } else {
                                snake.setDirection(Direction.DOWN);
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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        // Fundo preto preenchido
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        shapeRenderer.end();

        // Borda branca 1px inteiramente dentro do retângulo
        float lineWidth = 1f;
        float offset = lineWidth / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(offset, offset, GameConfig.WORLD_WIDTH - lineWidth, GameConfig.WORLD_HEIGHT - lineWidth);
        shapeRenderer.end();

        // Desenhar cobra e comida
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        snake.draw(shapeRenderer);
        drawFood();
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
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
    protected abstract void initTime();

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

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
