package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import oop.graphics.Graphics;

public class PowerAvatar extends AnimationAvatar {

	private static final String SPRITE_SHEET = "src/game/gradius/graphics/vic_viper.png";

	public PowerAvatar(Entity entity) {
		super(entity, SPRITE_SHEET, 1, 1);
	}

	@Override
	public void initImage(Graphics g) {
		this.initImage(g,
				new ImageSpriteRect[] { new ImageSpriteRect(28, 149, 16, 16), new ImageSpriteRect(47, 149, 16, 16),
						new ImageSpriteRect(67, 150, 14, 14), new ImageSpriteRect(86, 150, 14, 14),
						new ImageSpriteRect(103, 151, 12, 12), new ImageSpriteRect(119, 151, 12, 12),
						new ImageSpriteRect(136, 152, 10, 10), new ImageSpriteRect(152, 152, 10, 10),
						new ImageSpriteRect(169, 153, 8, 8) });
	}
}