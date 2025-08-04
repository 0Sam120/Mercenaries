// AmmoDisplay.java
package come.onto.merc.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class AmmoDisplay {
    private BitmapFont font;
    private float x, y;

    public AmmoDisplay(float x, float y) {
        this.x = x;
        this.y = y;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);  // Make text larger
    }

    public void render(SpriteBatch batch, int currentAmmo, int reserveAmmo, boolean isReloading) {
        String ammoText = currentAmmo + " / " + reserveAmmo;
        if (isReloading) {
            ammoText += " (Reloading...)";
        }
        font.draw(batch, ammoText, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}
