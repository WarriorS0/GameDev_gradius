package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.AnimationAvatar;
import oop.graphics.Graphics;

public class ShipAvatar extends AnimationAvatar {

	public ShipAvatar(Entity entity) {
		super(entity, "src/game/gradius/graphics/vic_viper.png",1,1);
	}


	@Override
	public void initImage(Graphics g) {
		this.initImage(g,
				new ImageSpriteRect[] { new ImageSpriteRect(18, 13, 34, 18), new ImageSpriteRect(53, 13, 34, 18),
						new ImageSpriteRect(88, 13, 34, 18), new ImageSpriteRect(123, 13, 34, 18),
						new ImageSpriteRect(158, 13, 34, 18) });
	}
}