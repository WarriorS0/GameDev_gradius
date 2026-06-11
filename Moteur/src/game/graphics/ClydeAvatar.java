package game.graphics;

import engine.entity.Entity;

public class ClydeAvatar extends GhostAvatar {

	public ClydeAvatar(Entity entity) {
		super(entity);
	}

	@Override
	protected int normalY() {
		return 112;
	}
}