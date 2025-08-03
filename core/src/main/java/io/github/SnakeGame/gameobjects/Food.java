package io.github.SnakeGame.gameobjects;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Food {
    private Vector2 position;
    private int id;

    private static final int GRID_SIZE = 20;
    private static final int GRID_WIDTH = Gdx.graphics.getWidth() / GRID_SIZE;
    private static final int GRID_HEIGHT = Gdx.graphics.getHeight() / GRID_SIZE;
    private static final GlyphLayout layout = new GlyphLayout();

    public Food(int id) {
        this.id = id;
        position = new Vector2();
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
        renderer.rect(position.x * GRID_SIZE, position.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        renderer.setColor(Color.WHITE);
    }

    public void drawNumber(SpriteBatch batch, BitmapFont font) {
        String text = String.valueOf(id);

        layout.setText(font, text); // calcula largura/altura

        float textWidth = layout.width;
        float textHeight = layout.height;

        float x = position.x * GRID_SIZE + (GRID_SIZE - textWidth) / 2f;
        float y = position.y * GRID_SIZE + (GRID_SIZE + textHeight) / 2f;

        font.draw(batch, text, x, y);
    }

    public void respawn() {
        int x = MathUtils.random(0, GRID_WIDTH - 1);
        int y = MathUtils.random(0, GRID_HEIGHT - 1);
        position.set(x, y);
    }

    public void respawn(ArrayList<Food> existingFoods, Array<Vector2> snakeBody) {
        boolean positionOk;
        do {
            int x = MathUtils.random(0, GRID_WIDTH - 1);
            int y = MathUtils.random(0, GRID_HEIGHT - 1);
            position.set(x, y);

            positionOk = true;

            // Verifica colisão com outras comidas
            for (Food f : existingFoods) {
                if (f != this && f.getPosition().epsilonEquals(position, 0.01f)) {
                    positionOk = false;
                    break;
                }
            }

            // Verifica colisão com o corpo da cobra
            if (positionOk) {
                for (Vector2 part : snakeBody) {
                    if (part.epsilonEquals(position, 0.01f)) {
                        positionOk = false;
                        break;
                    }
                }
            }

        } while (!positionOk);
    }

    public void respawn(Array<Vector2> snakeBody) {
        boolean positionOk;
        do {
            int x = MathUtils.random(0, GRID_WIDTH - 1);
            int y = MathUtils.random(0, GRID_HEIGHT - 1);
            position.set(x, y);

            positionOk = true;

            // Verifica colisão com o corpo da cobra
            if (positionOk) {
                for (Vector2 part : snakeBody) {
                    if (part.epsilonEquals(position, 0.01f)) {
                        positionOk = false;
                        break;
                    }
                }
            }

        } while (!positionOk);
    }
}
