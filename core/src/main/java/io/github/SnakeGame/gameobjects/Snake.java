package io.github.SnakeGame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.SnakeGame.GameConfig;

public class Snake {
    private Array<Vector2> body = new Array<>();
    private Direction direction = Direction.RIGHT;

    private Sound sound;

    public Snake() {
        body.add(new Vector2(5, 5)); // posição inicial em células
        sound = Gdx.audio.newSound(Gdx.files.internal("music/select.mp3"));
    }

    public void move() {
        Vector2 head = body.first().cpy();
        switch (direction) {
            case UP:    head.y += 1; break;
            case DOWN:  head.y -= 1; break;
            case LEFT:  head.x -= 1; break;
            case RIGHT: head.x += 1; break;
        }
        body.insert(0, head);
        body.pop();
    }

    public void grow() {
        body.add(new Vector2(body.peek()));
    }

    public int getLength() {
        return body.size;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.GREEN);
        for (Vector2 part : body) {
            renderer.rect(part.x * GameConfig.CELL_SIZE, part.y * GameConfig.CELL_SIZE, GameConfig.CELL_SIZE, GameConfig.CELL_SIZE);
        }
    }

    public void setDirection(Direction newDirection) {
        if (!newDirection.isOpposite(direction)) {
            direction = newDirection;
        }
    }

    public boolean isCollidingWith(Food food) {
        boolean collide = body.first().epsilonEquals(food.getPosition(), 0.1f);
        if (collide) {
            long id = sound.play();
            sound.setVolume(id, 0.5f);
        }
        return collide;
    }

    public boolean isCollidingWithSelf() {
        Vector2 head = body.first();
        for (int i = 1; i < body.size; i++) {
            if (head.epsilonEquals(body.get(i), 0.1f)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOutOfBounds() {
        Vector2 head = body.first();
        return head.x < 0 || head.x >= GameConfig.GRID_WIDTH || head.y < 0 || head.y >= GameConfig.GRID_HEIGHT;
    }

    public Array<Vector2> getBody() {
        return body;
    }
}
