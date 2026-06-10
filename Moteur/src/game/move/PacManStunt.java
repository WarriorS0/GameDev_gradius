package game.move;

import java.util.List;

import engine.Entity;
import engine.Grid.Cell;
import engine.ISU.Vector;
import game.entity.Ghost;

public class PacManStunt extends Stunt{
	public PacManStunt(Model model, Entity e) {
		super(model, e);
		entity.set(this);
		l = new Listener() {
			public void collision(Entity en) {
				if(en instanceof Ghost) {
					entity.avatar().dead = true;
				}
				entity.set_aSpeed(0);
				entity.set(entity.isu.new Vector(0, 0));
				System.out.println("ATTENTION COLLISION");
			}
		};
	}

	@Override
	public void set(int orientation) {
		entity.set(orientation);
		if (orientation >= 0 && orientation < 90) {
			set(entity.isu.new Vector(20,0));
		} else if (orientation >= 90 && orientation < 180) {
			set(entity.isu.new Vector(0,20));
		} else if (orientation >= 180 && orientation < 270) {
			set(entity.isu.new Vector(-20,0));
		} else {
			set(entity.isu.new Vector(0,-20));
		}
	}

	@Override
	protected void set(Cell c) {
		entity.set(c);
		;

	}

	@Override
	protected void set(double x, double y) {
		entity.set(x, y);

	}

	@Override
	protected void collision(Entity e) {

	}

	@Override
	protected void collision(List<Entity> e) {

	}

	@Override
	public void set(Vector lSpeed) {
		entity.set(lSpeed);
	}

	@Override
	public void set_aSpeed(int aSpeed) {
		entity.set_aSpeed(aSpeed);
	}

}
