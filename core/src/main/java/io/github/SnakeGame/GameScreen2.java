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

    private float timeRemaining = 60f; // 60 segundos, por exemplo

    private float timer = 0;
    private final float MOVE_INTERVAL = 0.2f;

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
