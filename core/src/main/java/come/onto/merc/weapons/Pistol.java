// Pistol.java
package come.onto.merc.weapons;

import come.onto.merc.entities.Bullet;
import come.onto.merc.entities.Weapon;

public class Pistol extends Weapon {
    public Pistol(){
        super();
        shootCooldown = 0.1f;
        currentShootCooldown = 0;
        damage = 10;
        bulletSpeed = 2000f;

        // Initialize ammo-related values
        clipSize = 8;
        currentClipAmmo = clipSize;
        maxReserveAmmo = 48;
        currentReserveAmmo = maxReserveAmmo;
        reloadTime = 0.8f;

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
