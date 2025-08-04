// MyGame.java

package come.onto.merc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import come.onto.merc.managers.MusicManager;
import come.onto.merc.screens.GameScreen;
import come.onto.merc.screens.SplashScreen;

public class MyGame extends Game {
    private MusicManager musicManager;

    @Override
    public void create() {
        musicManager = new MusicManager();
        setScreen(new GameScreen(this));
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    @Override
    public void dispose() {
        super.dispose();
        musicManager.dispose();
    }
}
