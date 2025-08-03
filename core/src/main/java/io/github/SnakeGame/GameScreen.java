package io.github.SnakeGame;

public class GameScreen extends AbstractGameScreen {
    private Food food;

    public GameScreen(Main game) {
        super(game);
    }

    @Override
    protected void initFood() {
        food = new Food(0);
    }

    @Override
    protected void handleFoodCollisions() {
        if (snake.isCollidingWith(food)) {
            snake.grow();
            food.respawn(snake.getBody());
        }
    }

    @Override
    protected void drawFood() {
        food.draw(shapeRenderer);
    }

    @Override
    protected void drawUI() {
        // Nada para desenhar em UI nesse modo
    }
}
