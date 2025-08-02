package io.github.SnakeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class InicialGame implements Screen {
    private final Main game;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Skin skin;

    public InicialGame(Main game) {
        this.game = game;
        game.music.play();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // Use um skin pronto do LibGDX
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        Label title = new Label("Snake Game", skin, "default");
        title.setFontScale(5);
        title.setAlignment(Align.center);

        TextButton playButton = new TextButton("Jogar", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OpcionalGame(game));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(title).padBottom(30).row();
        table.add(playButton).width(200).height(60);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        drawSnakeGraphic(); // Desenha a cobra na tela inicial
    }

    private void drawSnakeGraphic() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        // Corpo da cobra (quadradinhos simples)
        for (int i = 0; i < 5; i++) {
            shapeRenderer.rect(50 + i * 22, 100, 20, 20);
        }

        shapeRenderer.end();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
