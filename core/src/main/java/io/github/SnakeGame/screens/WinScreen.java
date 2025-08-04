package io.github.SnakeGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import io.github.SnakeGame.Main;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class WinScreen implements Screen {
    private final Main game;
    
    private Skin skin;
    private Sound sound;
    private Stage stage;
    private SpriteBatch spriteBatch;
    
    private final int score;
    private Array<ParticleWrapper> effects;

    private static class ParticleWrapper {
        ParticleEffect effect;

        public ParticleWrapper(ParticleEffect effect) {
            this.effect = effect;
        }
    }

    public WinScreen(Main game, int score) {
        this.game = game;
        this.score = score;
    }

    @Override
    public void show() {
        sound = Gdx.audio.newSound(Gdx.files.internal("music/winning.mp3"));
        long soundId = sound.play();
        sound.setVolume(soundId, 0.3f);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        spriteBatch = new SpriteBatch();
        effects = new Array<>();
        if (!Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
            spawnNewEffects(); // só ativa partículas fora do HTML5
        }

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Label winLabel = new Label("Você Venceu!", skin, "default");
        winLabel.setFontScale(3);
        winLabel.setColor(0, 1, 0, 1);
        winLabel.setAlignment(Align.center);

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
                game.setScreen(new InicialGameScreen(game));
                game.telaAnterior.dispose();
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(winLabel).padBottom(20).row();
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

        if (!Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
            spriteBatch.begin();

            Array<ParticleWrapper> finished = new Array<>();
            for (ParticleWrapper wrapper : effects) {
                ParticleEffect effect = wrapper.effect;
                effect.update(delta);
                effect.draw(spriteBatch, delta);

                if (effect.isComplete()) {
                    finished.add(wrapper);
                }
            }

            // remove efeitos finalizados e cria novos
            for (ParticleWrapper wrapper : finished) {
                effects.removeValue(wrapper, true);
                wrapper.effect.dispose();
                spawnOneEffect();
            }

            spriteBatch.end();
        }
    }

    private void spawnOneEffect() {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/particle-animation.p"), Gdx.files.internal("particles/"));

        float x, y;
        do {
            x = (float) Math.random() * Gdx.graphics.getWidth();
            y = (float) Math.random() * Gdx.graphics.getHeight();
        } while (x > Gdx.graphics.getWidth() * 0.3f && x < Gdx.graphics.getWidth() * 0.7f &&
                y > Gdx.graphics.getHeight() * 0.3f && y < Gdx.graphics.getHeight() * 0.7f);

        effect.setPosition(x, y);
        effect.start();
        effects.add(new ParticleWrapper(effect));
    }


    private void spawnNewEffects() {
        effects.clear();
        for (int i = 0; i < 2; i++) {
            spawnOneEffect();
        }
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        sound.stop();
        sound.dispose();
        stage.dispose();
        skin.dispose();
        spriteBatch.dispose();
        for (ParticleWrapper wrapper : effects) {
            wrapper.effect.dispose();
        }
    }
}
