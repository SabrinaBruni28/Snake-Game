package io.github.SnakeGame.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import io.github.SnakeGame.Main;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOverScreen implements Screen {
    private final Main game;
    private final int score;

    private Stage stage;
    private Skin skin;
    private Sound sound;

    public GameOverScreen(Main game, int score) {
        this.game = game;
        this.score = score;
    }

    @Override
    public void show() {
        sound = Gdx.audio.newSound(Gdx.files.internal("music/game-over.mp3"));
        long soundId = sound.play();
        sound.setVolume(soundId, 0.3f);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Label gameOverLabel = new Label("Game Over", skin, "default");
        gameOverLabel.setFontScale(3);
        gameOverLabel.setColor(1, 0, 0, 1);
        gameOverLabel.setAlignment(Align.center);

        Label scoreLabel = new Label("Pontuação: " + score, skin);
        scoreLabel.setFontScale(1.5f);

        TextButton retryButton = new TextButton("Jogar Novamente", skin);
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.telaAnterior);
                dispose();
            }
        });

        TextButton exitButton = new TextButton("Sair", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InicialGame(game));
                game.telaAnterior.dispose();
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(gameOverLabel).padBottom(20).row();
        table.add(scoreLabel).padBottom(30).row();
        table.add(retryButton).width(250).height(60).padBottom(15).row();
        table.add(exitButton).width(250).height(60);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
