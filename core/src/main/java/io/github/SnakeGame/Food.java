package io.github.SnakeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Food {
    private Vector2 position;
    private static final int GRID_SIZE = 20; // tamanho de cada célula
    private static final int GRID_WIDTH = Gdx.graphics.getWidth() / GRID_SIZE;
    private static final int GRID_HEIGHT = Gdx.graphics.getHeight() / GRID_SIZE;

    public Food() {
        position = new Vector2();
        respawn();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);
        renderer.rect(position.x * GRID_SIZE, position.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        renderer.setColor(Color.WHITE);
    }

    public void respawn() {
        int x = MathUtils.random(0, GRID_WIDTH - 1);
        int y = MathUtils.random(0, GRID_HEIGHT - 1);
        position.set(x, y);
    }
}


