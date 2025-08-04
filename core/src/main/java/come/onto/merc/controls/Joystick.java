// Joystick.java
package come.onto.merc.controls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Joystick {
    private Texture baseTexture;
    private Texture knobTexture;
    private Vector2 center;
    private Vector2 knob;
    private boolean active;
    private float radius;
    private Circle bounds;

    public Joystick(float x, float y, float radius) {
        baseTexture = new Texture("joystick_base.png");
        knobTexture = new Texture("joystick_knob.png");
        center = new Vector2(x, y);
        knob = new Vector2(x, y);
        this.radius = radius;
        bounds = new Circle(x, y, radius);
        active = false;
    }

    public boolean handleTouch(Vector2 touchPos) {
        float distance = touchPos.dst(center);
        if (distance <= radius * 2) {
            active = true;
            knob.set(touchPos);

            if (distance > radius) {
                knob.sub(center).nor().scl(radius).add(center);
            }
            return true;
        }
        return false;
    }

    public Vector2 getPosition() {
        return new Vector2(bounds.x, bounds.y);
    }

    public float getRadius() {
        return bounds.radius;
    }

    public void reset() {
        active = false;
        knob.set(center);
    }

    public void render(SpriteBatch batch) {
        // Draw base
        batch.draw(baseTexture,
            center.x - baseTexture.getWidth()/2,
            center.y - baseTexture.getHeight()/2);

        // Draw knob
        batch.draw(knobTexture,
            knob.x - knobTexture.getWidth()/2,
            knob.y - knobTexture.getHeight()/2);
    }

    public Vector2 getDirection() {
        return new Vector2(knob).sub(center).nor();
    }

    public float getIntensity() {
        return Math.min(knob.dst(center) / radius, 1f);
    }

    public float getAngle() {
        return new Vector2(knob).sub(center).angle();
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        baseTexture.dispose();
        knobTexture.dispose();
    }
}
