package game.graphics;

import engine.entity.Entity;

public class BlinkyAvatar extends GhostAvatar {

	public BlinkyAvatar(Entity entity) {
		super(entity);
	}

	@Override
	protected int normalY() {
		return 62;
	}

	@Override
	protected int normalDownY() {
		return 63;
	}

	@Override
	protected int normalHeight() {
		return 18;
	}
}