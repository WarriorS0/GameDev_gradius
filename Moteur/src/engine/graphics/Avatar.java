package engine.graphics;

import engine.entity.Entity;
import oop.graphics.Graphics;

public abstract class Avatar {
	
	final static int MAX_ZORDER = 9;
	protected Entity entity;
	private int z_order;

	protected Avatar(Entity entity) {
		this.entity = entity;
		z_order =  0;
	}

	public Entity entity() {
		return entity;
	}
	
	public int z_order() {
		return z_order;
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
	
	protected void set_z_order(int z_order) {
		if(z_order > MAX_ZORDER)
			throw new IllegalArgumentException("The maximum Z_order is "+MAX_ZORDER);
		this.z_order = z_order;
	}

	public abstract void paint(Graphics g);

	public abstract void updateAnimation(double delta_t);

	public abstract void initImages(Graphics g);
}