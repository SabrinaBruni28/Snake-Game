package io.github.SnakeGame.gamemodes;

import java.util.Iterator;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import io.github.SnakeGame.Main;
import io.github.SnakeGame.gameobjects.Food;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class OrderedTimedModeScreen extends AbstractGameScreen {
    private ArrayList<Food> food;

    private int currentTargetId = 1;
    private float timeRemaining = 180f;

    private BitmapFont fontSmall;
    private BitmapFont fontLarge;

    public OrderedTimedModeScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(1f);

        fontLarge = new BitmapFont();
        fontLarge.getData().setScale(1.3f);
    }

    @Override
    protected void initFood() {
        food = new ArrayList<Food>();
        for (int i = 0; i < 30; i++) {
            Food newFood = new Food(i + 1);
            newFood.respawn(food, snake.getBody());
            food.add(newFood);
        }

        food.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
    }

    @Override
    protected void initTime() {
        currentTargetId = 1;
        timeRemaining = 180f; // 3 minutos
    }

    @Override
    protected void handleFoodCollisions() {
        Iterator<Food> iterator = food.iterator();
        while (iterator.hasNext()) {
            Food f = iterator.next();
            if (snake.isCollidingWith(f)) {
                if (f.getId() == currentTargetId) {
                    snake.grow();
                    iterator.remove();
                    currentTargetId++;

                    if (food.isEmpty()) {
                        endGame(true);
                    }
                } 
                else {
                    // Comeu a comida errada → Game Over
                    endGame(false);
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

        for (Food f : food) {
            f.drawNumber(batch, fontSmall);
        }
        fontLarge.draw(batch, "Tempo: " + (int) timeRemaining, 10, Gdx.graphics.getHeight() - 10);

        if (timeRemaining <= 0) {
            endGame(false);
            return;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        fontSmall.dispose();
        fontLarge.dispose();
    }
}
