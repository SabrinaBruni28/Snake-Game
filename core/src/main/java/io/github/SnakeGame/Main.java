package io.github.SnakeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import io.github.SnakeGame.screens.InicialGameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Music music;
    public Sound sound;
    public Screen telaAnterior;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        sound = Gdx.audio.newSound(Gdx.files.internal("music/click.wav"));
        setScreen(new InicialGameScreen(this));
    }

    @Override
    public void dispose() {
        if (music != null) {
            music.stop();
            music.dispose();   
        }
        if (getScreen() != null) {
            getScreen().dispose();
        }
    } 
}