package io.github.SnakeGame.gameobjects;

import java.util.ArrayList;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.SnakeGame.GameConfig;

public class Food {
    private int id;
    private Vector2 position = new Vector2();

    private static final GlyphLayout layout = new GlyphLayout();

    public Food(int id) {
        this.id = id;
        respawn();
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);
        renderer.rect(position.x * GameConfig.CELL_SIZE, position.y * GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
        renderer.setColor(Color.WHITE);
    }

    public void drawNumber(SpriteBatch batch, BitmapFont font) {
        String text = String.valueOf(id);
        layout.setText(font, text);

        float textWidth = layout.width;
        float textHeight = layout.height;

        float x = position.x * GameConfig.CELL_SIZE + (GameConfig.CELL_SIZE - textWidth) / 2f;
        float y = position.y * GameConfig.CELL_SIZE + (GameConfig.CELL_SIZE + textHeight) / 2f;

        font.draw(batch, text, x, y);
    }

    public void respawn() {
        int x = MathUtils.random(0, GameConfig.GRID_WIDTH - 1);
        int y = MathUtils.random(0, GameConfig.GRID_HEIGHT - 1);
        position.set(x, y);
    }

    public void respawn(ArrayList<Food> existingFoods, Array<Vector2> snakeBody) {
        boolean valid;
        do {
            int x = MathUtils.random(0, GameConfig.GRID_WIDTH - 1);
            int y = MathUtils.random(0, GameConfig.GRID_HEIGHT - 1);
            position.set(x, y);
            valid = true;

            for (Food f : existingFoods) {
                if (f != this && f.getPosition().epsilonEquals(position, 0.01f)) {
                    valid = false;
                    break;
                }
            }

            for (Vector2 part : snakeBody) {
                if (part.epsilonEquals(position, 0.01f)) {
                    valid = false;
                    break;
                }
            }

        } while (!valid);
    }

    public void respawn(Array<Vector2> snakeBody) {
        boolean valid;
        do {
            int x = MathUtils.random(0, GameConfig.GRID_WIDTH - 1);
            int y = MathUtils.random(0, GameConfig.GRID_HEIGHT - 1);
            position.set(x, y);
            valid = true;

            for (Vector2 part : snakeBody) {
                if (part.epsilonEquals(position, 0.01f)) {
                    valid = false;
                    break;
                }
            }

        } while (!valid);
    }
}
