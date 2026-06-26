package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import oop.graphics.Graphics;

public class Laser1Avatar extends AnimationAvatar {

	public Laser1Avatar(Entity entity) {
		super(entity, "src/game/gradius/graphics/small_enemies.png", 1, 1);
	}

	@Override
	public void initImage(Graphics g) {
		this.initImage(g,
				new ImageSpriteRect[] { new ImageSpriteRect(126, 121, 10, 4), new ImageSpriteRect(137, 121, 10, 4) });
	}

}
