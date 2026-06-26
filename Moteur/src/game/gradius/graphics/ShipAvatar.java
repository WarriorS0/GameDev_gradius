package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import game.gradius.entity.Ship;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class ShipAvatar extends AnimationAvatar {

	private BufferedImage[] aliveFrames;
	private BufferedImage[] deathFrames;

	public ShipAvatar(Entity entity) {
		super(entity, "src/game/gradius/graphics/vic_viper.png", 1, 1);
	}

	@Override
	public void initImage(Graphics g) {
		if (hasBeenInitialized) {
			return;
		}

		this.initImage(g,
				new ImageSpriteRect[] {
						new ImageSpriteRect(18, 13, 34, 18),
						new ImageSpriteRect(53, 13, 34, 18),
						new ImageSpriteRect(88, 13, 34, 18),
						new ImageSpriteRect(123, 13, 34, 18),
						new ImageSpriteRect(158, 13, 34, 18)
				});

		aliveFrames = frames;

		deathFrames = new BufferedImage[] {
				image.getSubimage(145, 93, 14, 14),
				image.getSubimage(163, 91, 24, 18)
		};
	}

	@Override
	public void updateAnimation(double delta_t) {
		if (entity() instanceof Ship ship && ship.deathAnimationPlaying()) {
			frames = deathFrames;

			int index = (int) (ship.deathAnimationProgress() * deathFrames.length);
			frameIndex = Math.min(deathFrames.length - 1, index);

			return;
		}

		if (frames != aliveFrames) {
			frames = aliveFrames;
			frameIndex = 0;
			time = 0.0;
		}

		super.updateAnimation(delta_t);
	}
}