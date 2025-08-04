// BasicGun.java
package come.onto.merc.weapons;

import come.onto.merc.entities.Bullet;
import come.onto.merc.entities.Weapon;

public class BasicGun extends Weapon {
    public BasicGun() {
        super();
        shootCooldown = 0.25f; // Can shoot 4 times per second
        currentShootCooldown = 0;
        damage = 25;
        bulletSpeed = 2500f;

        // Initialize ammo-related values
        clipSize = 12;
        currentClipAmmo = clipSize;
        maxReserveAmmo = 60;
        currentReserveAmmo = maxReserveAmmo;
        reloadTime = 1.5f;

        Bullet.init();
    }

    @Override
    public void shoot(float rotation) {
        if (currentShootCooldown <= 0 && !isReloading) {
            bullets.add(new Bullet(position.x, position.y, rotation, damage, bulletSpeed));
            currentClipAmmo--;
            currentShootCooldown = shootCooldown;

            // Auto-reload when clip is empty
            if (currentClipAmmo == 0 && currentReserveAmmo > 0) {
                startReload();
            }
        }
    }
}
