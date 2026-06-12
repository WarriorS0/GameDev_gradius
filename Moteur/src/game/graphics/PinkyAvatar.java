package game.graphics;

import engine.entity.Entity;

public class PinkyAvatar extends GhostAvatar {

	public PinkyAvatar(Entity entity) {
		super(entity);
	}

	@Override
	protected int normalY() {
		return 80;
	}

	@Override
	protected int normalDownY() {
		return 81;
	}
}