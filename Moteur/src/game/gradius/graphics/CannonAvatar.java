package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import oop.graphics.Graphics;

public class CannonAvatar extends AnimationAvatar {

	public CannonAvatar(Entity entity) {
		super(entity, "src/game/gradius/graphics/cannon_sprites.png", 2, 2);
	}

	@Override
	public void initImage(Graphics g) {
		this.initImage(g, new ImageSpriteRect[] { new ImageSpriteRect(24, 19, 14, 5),
				new ImageSpriteRect(42, 19, 14, 5), new ImageSpriteRect(60, 19, 14, 5) });
	}
}