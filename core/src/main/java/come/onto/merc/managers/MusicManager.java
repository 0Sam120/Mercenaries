package come.onto.merc.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;

public class MusicManager {
    private Music currentMusic;
    private float currentVolume = 0.5f; // Default volume

    public void play(String filePath, boolean looping, float volume) {
        // If the current music is already playing the same file, just adjust settings
        if (currentMusic != null && currentMusic.isPlaying() && Gdx.files.internal(filePath).path().equals(currentMusic.toString())) {
            currentMusic.setVolume(volume); // Update volume if necessary
            currentMusic.setLooping(looping); // Update looping if necessary
            return;
        }

        // Stop and dispose of the current music
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }

        // Load and play the new music
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        currentMusic.setLooping(looping);
        currentVolume = volume;
        currentMusic.setVolume(currentVolume);
        currentMusic.play();
    }

    public void stop() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
    }

    public void setVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
            currentVolume = volume;
        }
    }

    public float getVolume() {
        return currentVolume;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    // Ensures music continues playing when switching screens (unless stopped explicitly)
    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }
}
