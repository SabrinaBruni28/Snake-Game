package io.github.SnakeGame;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Snake {
    private Array<Vector2> body = new Array<>();
    private Direction direction = Direction.RIGHT;

    public Snake() {
        body.add(new Vector2(5, 5));
    }

    public void move() {
        Vector2 head = body.first().cpy();
        switch (direction) {
            case UP: head.y += 1; break;
            case DOWN: head.y -= 1; break;
            case LEFT: head.x -= 1; break;
            case RIGHT: head.x += 1; break;
        }
        body.insert(0, head);
        body.pop();
    }

    public void grow() {
        body.add(new Vector2(body.peek()));
    }

    public void draw(ShapeRenderer renderer) {
        for (Vector2 part : body) {
            renderer.rect(part.x * 20, part.y * 20, 20, 20);
        }
    }

    public void setDirection(Direction newDirection) {
        if (!newDirection.isOpposite(direction)) {
            direction = newDirection;
        }
    }

    public boolean isCollidingWith(Food food) {
        return body.first().epsilonEquals(food.getPosition(), 0.1f);
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

    public boolean isOutOfBounds(int width, int height) {
        Vector2 head = body.first();
        return head.x < 0 || head.x >= width || head.y < 0 || head.y >= height;
    }
}
