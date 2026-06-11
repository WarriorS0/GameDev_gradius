package engine.move;

import java.util.List;

import engine.entity.Entity;
import engine.geometry.Grid.Cell;
import engine.geometry.ISU;
import engine.geometry.ISU.Vector;
import game.Game;

public abstract class Stunt {

	protected final Model model;
	protected final Entity entity;

	protected ISU.Vector targetDirection;
	protected double targetAngle;

	public final Listener listener;

	protected Stunt(Model model, Entity entity) {
		this.model = model;
		this.entity = entity;

		this.targetDirection = Game.game().isu.new Vector(0, 0);
		this.targetAngle = entity.orientation();

		this.listener = new Listener();
	}

	public void set(int orientation) {
		this.targetAngle = normalizeAngle(orientation);

		double deltaAngle = this.targetAngle - entity.orientation();
		entity.turn(deltaAngle);
	}

	
	protected abstract void set(Cell cell);

	
	protected void set(double x, double y) {
		entity.place(Game.game().isu.new Coord(x, y));
	}

	
	public void set(Vector linearSpeed) {
		this.targetDirection = linearSpeed;
		model.setLinearSpeed(entity, linearSpeed);
	}

	
	public void set_aSpeed(int angularSpeed) {
		model.setAngularSpeed(entity, angularSpeed);
	}

	protected abstract void collision(Entity entity);

	protected abstract void collision(List<Entity> entities);

	private double normalizeAngle(double angle) {
		double normalized = angle % 360.0;

		if (normalized < 0) {
			normalized += 360.0;
		}

		return normalized;
	}

	public class Listener {

		public void collision(Entity other) {
			Stunt.this.collision(other);
		}

		public void collision(List<Entity> others) {
			Stunt.this.collision(others);
		}
	}
}