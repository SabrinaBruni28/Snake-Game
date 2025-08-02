package io.github.SnakeGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Music music;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
        setScreen(new InicialGame(this));
    }

    @Override
    public void dispose() {
        if (music != null) {
            music.stop();
            music.dispose();   
        }
    } 
}