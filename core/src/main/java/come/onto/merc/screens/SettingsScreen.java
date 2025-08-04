package come.onto.merc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import come.onto.merc.MyGame;

public class SettingsScreen implements Screen {
    private MyGame game;
    private Stage stage;
    private boolean transitioning = false;
    private SpriteBatch batch;
    private Texture backgroundImage;

    public SettingsScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundImage = new Texture("main_menu_background.png");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Table table = new Table();
        table.setFillParent(true);

        // Volume Slider
        Slider volumeSlider = new Slider(0, 1, 0.1f, false, skin);
        volumeSlider.setValue(game.getMusicManager().getVolume());
        volumeSlider.addListener(event -> {
            game.getMusicManager().setVolume(volumeSlider.getValue());
            return false;
        });
        table.add(volumeSlider).size(100).padBottom(20);

        // Back Button
        TextButton backButton = new TextButton("Back", skin);
        // Back Button Listener
        backButton.addListener(event -> {
            if (!event.isHandled()) {
                slideOutAndSwitch(new MainMenuScreen(game), Gdx.graphics.getHeight());
            }
            return false;
        });
        table.add(backButton).size(300, 80);

        stage.addActor(table);

        // Resume music only if it's not playing
        if (!game.getMusicManager().isPlaying()) {
            game.getMusicManager().resumeMusic();
        }
    }

    private void slideOutAndSwitch(Screen targetScreen, float slideAmount) {
        if (transitioning) {
            return; // Prevent duplicate transitions
        }

        transitioning = true;

        stage.addAction(Actions.sequence(
            Actions.moveBy(0, slideAmount, 0.5f),
            Actions.run(() -> {
                game.setScreen(targetScreen);
                transitioning = false;
            })
        ));
    }



    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

}
