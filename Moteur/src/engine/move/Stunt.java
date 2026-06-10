package game.move;

import java.util.List;

import engine.Entity;
import engine.Grid.Cell;
import engine.ISU;
import engine.ISU.Vector;

public abstract class Stunt {
	public Listener l;
	Model model;
	Entity entity;
	ISU.Vector target_direction;
	int target_angle;
	public Stunt(Model model, Entity e) {
		this.model = model;
		entity = e;
	}
	public abstract void set(int orientation);
	protected abstract void set(Cell c);
	protected abstract void set(double x,double y);
	protected abstract void collision(Entity e);
	protected abstract void collision(List<Entity> e);
	public abstract void set(Vector lSpeed);
	public void set_aSpeed(int aSpeed) {
		// TODO Auto-generated method stub
		
	}
	public static class Listener {
		public void collision(Entity e) {
		}
	}
}





























