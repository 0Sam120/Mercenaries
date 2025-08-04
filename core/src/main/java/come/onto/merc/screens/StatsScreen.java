// StatsScreen.java
package come.onto.merc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StatsScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private int score;
    private int highestCombo;
    private String grade;

    public StatsScreen(int score, int highestCombo) {
        this.score = score;
        this.highestCombo = highestCombo;

        // Calculate grade based on score
        if (score >= 5000) grade = "S";
        else if (score >= 4000) grade = "A";
        else if (score >= 3000) grade = "B";
        else if (score >= 2000) grade = "C";
        else grade = "D";

        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        batch.begin();

        // Display stats
        font.setColor(Color.WHITE);
        font.getData().setScale(3);
        font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 100);
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 200);
        font.draw(batch, "Highest Combo: " + highestCombo, Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 300);
        font.draw(batch, "Grade: " + grade, Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 400);

        // Display buttons
        font.draw(batch, "Quit", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 600);

        batch.end();

        // Handle input
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y-coordinate

            // Quit button
            if (touchX >= Gdx.graphics.getWidth() / 2 - 100 && touchX <= Gdx.graphics.getWidth() / 2 + 100 &&
                touchY >= Gdx.graphics.getHeight() - 600 && touchY <= Gdx.graphics.getHeight() - 550) {
                Gdx.app.exit();
            }
        }
    }

    // Other required methods
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

