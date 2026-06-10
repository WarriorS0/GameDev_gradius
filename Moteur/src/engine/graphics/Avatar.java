package engine.graphics;

import engine.entity.Entity;
import oop.graphics.Graphics;

public abstract class Avatar {
	public Entity e;
	public boolean dead;
	public Avatar(Entity e) {
		this.e = e;
		e.setAvatar(this);
		dead = false;
	}
	
	public abstract void paint(Graphics g);
	public abstract void updateAnimation(double delta_t);
	public abstract void initImages(Graphics g);

}



























