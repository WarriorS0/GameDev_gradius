package engine.graphics;

import engine.entity.Entity;
import oop.graphics.Graphics;

public abstract class Avatar {

	protected Entity entity;

	protected Avatar(Entity entity) {
		this.entity = entity;
	}

	public Entity entity() {
		return entity;
	}

	public boolean dead() {
		return entity != null && entity.dead();
	}

	public void kill() {
		if (entity != null) {
			entity.kill();
		}
	}

	public void revive() {
		if (entity != null) {
			entity.revive();
		}
	}

	public abstract void paint(Graphics g);

	public abstract void updateAnimation(double delta_t);

	public abstract void initImages(Graphics g);
}