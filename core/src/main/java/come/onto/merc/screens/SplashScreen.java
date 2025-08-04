// SplashScreen.java
package come.onto.merc.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import come.onto.merc.MyGame;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture splashImage;
    private float displayTime = 3f;  // Time to show the splash screen (seconds)
    private float elapsedTime = 0f;
    private MyGame game; // Reference to the main game class

    public SplashScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        splashImage = new Texture("libgdx.png"); // Add splash image to assets folder
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(splashImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (elapsedTime >= displayTime) {
            game.setScreen(new MainMenuScreen(game)); // Transition to the main menu
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        splashImage.dispose();
    }
}
