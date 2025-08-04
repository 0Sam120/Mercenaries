// WeaponSwitchButton.java
package come.onto.merc.controls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class WeaponSwitchButton {
    float x, y, width, height;
    private Texture texture;
    private String label;

    public WeaponSwitchButton(float x, float y, float width, float height, String label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.texture = new Texture(label.toLowerCase() + "_icon.png"); // Placeholder for textures
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - width / 2, y - height / 2, width, height);
    }

    public boolean isTouched(float touchX, float touchY) {
        return touchX >= (x - width / 2) && touchX <= (x + width / 2) &&
            touchY >= (y - height / 2) && touchY <= (y + height / 2);
    }

    public float getY() {
        return this.y;
    }

    public float getHeight() {
        return this.height;
    }
}
