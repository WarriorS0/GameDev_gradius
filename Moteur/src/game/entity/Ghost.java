package game.entity;

import engine.entity.Entity;

// == GHOST ==

public class Ghost extends Entity {

	// CONSTRUCTOR

	public Ghost() {
		super("Ghost");
		this.setBounding();
	}

	// TASK COLLISION

	@Override
	protected void setBounding() {
		super.setBounding();

		// TODO:
		// Un Ghost est englobé par l'union d'un rectangle et d'un cercle.
	}

}
