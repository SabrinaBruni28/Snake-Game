package io.github.SnakeGame.gamemodes;

import io.github.SnakeGame.Main;
import io.github.SnakeGame.gameobjects.Food;

public class ClassicModeScreen extends AbstractGameScreen {
    private Food food;

    public ClassicModeScreen(Main game) {
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

            if (food.getPosition().x == -1) {
                endGame(true); // Vitória: sem espaço para mais comida
            }
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

    @Override
    protected void initTime() {
        // Nada para inicializar em tempo nesse modo
    }
}
