package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import oop.graphics.Graphics;

public class ProjectileAvatar extends AnimationAvatar {

	public ProjectileAvatar(Entity entity) {
		super(entity, "src/game/gradius/graphics/vic_viper.png", 1, 1);
	}

	@Override
	public void initImage(Graphics g) {
		this.initImage(g,
				new ImageSpriteRect[] { new ImageSpriteRect(126, 121, 10, 5), new ImageSpriteRect(137, 121, 10, 5) });
	}

}