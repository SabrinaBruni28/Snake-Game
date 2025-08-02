package io.github.SnakeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen implements Screen {
    private final Main game;
    private Snake snake;
    private Food food;
    private ShapeRenderer shapeRenderer;

    private float timer = 0;
    private final float MOVE_INTERVAL = 0.2f;
    private float touchStartX, touchStartY;
    private final float SWIPE_THRESHOLD = 50f; // distância mínima para considerar swipe

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        snake = new Snake();
        food = new Food(0);

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
        timer += delta;
        handleInput();

        if (timer >= MOVE_INTERVAL) {
            snake.move();
            
            if (snake.isCollidingWith(food)) {
                snake.grow();
                food.respawn();
            }
            
            else if (snake.isCollidingWithSelf() || snake.isOutOfBounds()) {
                int score = snake.getLength() - 1; // ou outra lógica de score
                game.setScreen(new GameOverScreen(game, score));
                dispose();
                return;
            }

            timer = 0;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            snake.draw(shapeRenderer);
            food.draw(shapeRenderer);
        shapeRenderer.end();
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
    }
}
