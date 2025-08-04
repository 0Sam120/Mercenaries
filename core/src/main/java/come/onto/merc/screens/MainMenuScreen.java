package come.onto.merc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import come.onto.merc.MyGame;

public class MainMenuScreen implements Screen {
    private MyGame game; // Reference to MyGame
    private SpriteBatch batch;
    private Texture backgroundImage;
    private Stage stage;
    private boolean inputEnabled = true;
    private float fadeInTime = 2f;
    private float alpha = 0f;

    public MainMenuScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundImage = new Texture("main_menu_background.png");

        inputEnabled = false; // Disable input at the start

        // Play menu music
        if (!game.getMusicManager().isPlaying()) {
            game.getMusicManager().play("menu_music.mp3", true, game.getMusicManager().getVolume());
        }
        else{
            game.getMusicManager().resumeMusic();
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Table table = new Table();
        table.setFillParent(true);

        // Title
        TextButton title = new TextButton("Mercenaries", skin);
        title.setDisabled(true);
        table.add(title).size(1300, 480).padBottom(100).row();

        // Play Button
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(event -> {
            if (!event.isHandled() && inputEnabled) {
                stage.clear();
                skin.dispose();
                game.setScreen(new GameScreen(game));
            }
            return false;
        });
        table.add(playButton).size(300, 80).padBottom(40).row();


        stage.addActor(table);
        stage.addAction(Actions.sequence(
            Actions.alpha(0), // Start fully transparent
            Actions.fadeIn(0.5f), // Fade in
            Actions.run(() -> inputEnabled = true) // Re-enable input after fade-in
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setColor(1, 1, 1, 1);
        batch.begin();
        batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
