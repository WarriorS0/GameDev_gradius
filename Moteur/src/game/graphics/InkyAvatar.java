package game.graphics;

import engine.entity.Entity;

public class InkyAvatar extends GhostAvatar {

	public InkyAvatar(Entity entity) {
		super(entity);
	}

	@Override
	protected int normalY() {
		return 95;
	}
}