package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import oop.graphics.Graphics;

public class EnemyAvatar extends AnimationAvatar {

	public EnemyAvatar(Entity entity) {
		super(entity, "src/game/gradius/graphics/small_enemies.png", 1, 1);
	}

	@Override
	public void initImage(Graphics g) {
		this.initImage(g, new ImageSpriteRect[] { new ImageSpriteRect(8, 21, 15, 15),
				new ImageSpriteRect(24, 21, 15, 15), new ImageSpriteRect(41, 21, 15, 15) });
	}
}
