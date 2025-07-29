package io.github.SnakeGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Food {
    private Vector2 position;

    public Food() {
        respawn();
    }

    public void respawn() {
        position = new Vector2(MathUtils.random(0, 19), MathUtils.random(0, 19));
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);
        renderer.rect(position.x * 20, position.y * 20, 20, 20);
        renderer.setColor(Color.WHITE);
    }

    public Vector2 getPosition() {
        return position;
    }
}

