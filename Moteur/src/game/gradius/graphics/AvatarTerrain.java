package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.avatars.SpriteAvatar;
import oop.graphics.Graphics;

public class AvatarTerrain extends SpriteAvatar {

	public AvatarTerrain() {
		this(null);
	}

	public AvatarTerrain(Entity entity) {
		super(entity, "src/game/graphics/pacman_sprite.png",1,1);
	}

	@Override
	public void initImage(Graphics g) {
		super.initImage(g, 250, 5, 20, 150);
	}

}