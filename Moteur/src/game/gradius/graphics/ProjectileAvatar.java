package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import game.gradius.entity.Projectile;
import oop.graphics.Graphics;

public class ProjectileAvatar extends AnimationAvatar {

	private static final String SPRITE_SHEET = "src/game/gradius/graphics/vic_viper.png";

	public ProjectileAvatar(Entity entity) {
		super(entity, SPRITE_SHEET, 1, 1);
	}

	@Override
	public void initImage(Graphics g) {
		if (entity() instanceof Projectile projectile && projectile.isBlueOrb()) {
			initBlueOrbImage(g);
		} else {
			initLaserImage(g);
		}
	}

	private void initLaserImage(Graphics g) {
		this.initImage(g,
				new ImageSpriteRect[] {
						new ImageSpriteRect(126, 121, 10, 5),
						new ImageSpriteRect(137, 121, 10, 5)
				});
	}

	private void initBlueOrbImage(Graphics g) {
		this.initImage(g,
				new ImageSpriteRect[] {
						new ImageSpriteRect(89, 93, 10, 8),
						new ImageSpriteRect(105, 92, 14, 10),
						new ImageSpriteRect(124, 91, 16, 12),
				});
	}
}