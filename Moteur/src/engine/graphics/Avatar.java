package engine.graphics;


import engine.entity.Entity;
import oop.graphics.Graphics;

public abstract class Avatar {

	protected Entity entity;
	private boolean dead;

	protected Avatar(Entity entity) {
		this.entity = entity;
		this.dead = false;
	}

	public Entity entity() {
		return entity;
	}

	public boolean dead() {
		return dead;
	}

	public void kill() {
		this.dead = true;
	}

	public void revive() {
		this.dead = false;
	}

	public abstract void paint(Graphics g);

	public abstract void updateAnimation(double delta_t);

	public abstract void initImages(Graphics g);
}