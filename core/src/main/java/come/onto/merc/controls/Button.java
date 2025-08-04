// Button.java
package come.onto.merc.controls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
    private Texture texture;
    private Rectangle bounds; // For rectangular buttons
    private Circle boundsCircle; // For circular buttons
    private boolean isCircular;
    private boolean isPressed;
    private boolean disabled;
    private float alpha;

    public Button(float x, float y, float radius, String texturePath) {
        this.texture = new Texture(texturePath);
        this.boundsCircle = new Circle(x, y, radius);
        this.isCircular = true; // Circular button
        this.isPressed = false;
        this.alpha = 0.5f; // Default transparency
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void render(SpriteBatch batch) {
        if (disabled) {
            batch.setColor(0.5f, 0.5f, 0.5f, alpha); // Greyed out
        } else {
            batch.setColor(1, 1, 1, alpha); // Normal appearance
        }

        if (isCircular) {
            batch.draw(texture,
                boundsCircle.x - boundsCircle.radius,
                boundsCircle.y - boundsCircle.radius,
                boundsCircle.radius * 2,
                boundsCircle.radius * 2);
        } else {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }

        batch.setColor(1, 1, 1, 1); // Reset color
    }

    public boolean isTouched(float touchX, float touchY) {
        if (isCircular) {
            return boundsCircle.contains(touchX, touchY);
        } else {
            return bounds.contains(touchX, touchY);
        }
    }

    public void handleTouch(Vector2 touch) {
        if (isTouched(touch.x, touch.y)) {
            isPressed = true;
        }
    }

    public void reset() {
        isPressed = false;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void dispose() {
        texture.dispose();
    }

    // Additional customization methods
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setTexture(String texturePath) {
        this.texture = new Texture(texturePath);
    }
}
