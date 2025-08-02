package io.github.SnakeGame;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Iterator;

public class GameScreen2 implements Screen {
    private final Main game;
    private Snake snake;
    private ArrayList<Food> food;
    private ShapeRenderer shapeRenderer;

    private float timeRemaining = 80f;

    private float timer = 0;
    private final float MOVE_INTERVAL = 0.2f;
    private float touchStartX, touchStartY;
    private final float SWIPE_THRESHOLD = 50f; // distância mínima para considerar swipe

    private SpriteBatch batch;
    private BitmapFont font;

    public GameScreen2(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // você pode carregar um customizado, se quiser
        font.getData().setScale(2f); // aumenta o tamanho da fonte
        shapeRenderer = new ShapeRenderer();
        snake = new Snake();
        food = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            food.add(new Food(i+1));
        }

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

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                        if (deltaX > 0) {
                            snake.setDirection(Direction.RIGHT);
                        } else {
                            snake.setDirection(Direction.LEFT);
                        }
                    }
                } else {
                    if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                        if (deltaY > 0) {
                            snake.setDirection(Direction.DOWN);
                        } else {
                            snake.setDirection(Direction.UP);
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        timeRemaining -= delta;
        if (timeRemaining <= 0) {
            int score = snake.getLength() - 1;
            game.setScreen(new GameOverScreen(game, score));
            dispose();
            return;
        }

        timer += delta;
        handleInput();

        if (timer >= MOVE_INTERVAL) {
            snake.move();

            if (snake.isCollidingWithSelf() || snake.isOutOfBounds()) {
                int score = snake.getLength() - 1;
                game.setScreen(new GameOverScreen(game, score));
                dispose();
                return;
            }

            Iterator<Food> iterator = food.iterator();
            while (iterator.hasNext()) {
                Food f = iterator.next();
                if (snake.isCollidingWith(f)) {
                    snake.grow();
                    iterator.remove(); // forma segura de remover

                    if (food.isEmpty()) {
                        int score = snake.getLength() - 1;
                        game.setScreen(new WinScreen(game, score));
                        dispose();
                        return;
                    }
                }
            }

            timer = 0;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        snake.draw(shapeRenderer);
        for (Food f : food) {
            f.draw(shapeRenderer);
        }
        shapeRenderer.end();

        // Mostrar o tempo
        batch.begin();
            font.draw(batch, "Tempo: " + (int)timeRemaining, 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) snake.setDirection(Direction.UP);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) snake.setDirection(Direction.DOWN);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) snake.setDirection(Direction.LEFT);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) snake.setDirection(Direction.RIGHT);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
