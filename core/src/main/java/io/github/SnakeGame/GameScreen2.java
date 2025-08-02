package io.github.SnakeGame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;

public class GameScreen2 extends AbstractGameScreen {
    private ArrayList<Food> food;
    private float timeRemaining = 75f;

    public GameScreen2(Main game) {
        super(game);
    }

    @Override
    protected void initFood() {
        food = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            food.add(new Food(i+1));
        }
    }

    @Override
    protected void handleFoodCollisions() {
        Iterator<Food> iterator = food.iterator();
        while (iterator.hasNext()) {
            Food f = iterator.next();
            if (snake.isCollidingWith(f)) {
                snake.grow();
                iterator.remove();
                if (food.isEmpty()) {
                    endGame(true);
                    return;
                }
            }
        }
    }

    @Override
    protected void drawFood() {
        for (Food f : food) {
            f.draw(shapeRenderer);
        }
    }

    @Override
    protected void drawUI() {
        timeRemaining -= Gdx.graphics.getDeltaTime();
        font.draw(batch, "Tempo: " + (int)timeRemaining, 10, Gdx.graphics.getHeight() - 10);
        if (timeRemaining <= 0) {
            endGame(false);
            return;
        }
    }
}
