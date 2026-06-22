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

	private boolean wantsMoveNotif = false;

	protected Stunt(Model model, Entity entity) {
		this.model = model;
		this.entity = entity;

		this.targetDirection = Game.game().isu.new Vector(0, 0);
		this.targetAngle = entity.orientation();
	}


	public void set(Vector linearSpeed) {
		this.targetDirection = linearSpeed;
		entity.setLinearSpeed(linearSpeed);
	}

	public void set_aSpeed(int angularSpeed) {
		entity.setAngularSpeed(angularSpeed);
	}

	public void setWantsMoveNotif(boolean wantsMoveNotif) {
		this.wantsMoveNotif = wantsMoveNotif;
	}

	public boolean wantsMoveNotif() {
		return this.wantsMoveNotif;
	}

	// LISTENER

	protected abstract void collision(Entity entity);

	protected abstract void collision(List<Entity> entities);

	protected abstract void moved(ISU.Coord oldPosition, ISU.Coord newPosition);

	protected abstract void rotated(double oldRotation, double newRotation);

	private double normalizeAngle(double angle) {
		double normalized = angle % 360.0;

		if (normalized < 0) {
			normalized += 360.0;
		}

		return normalized;
	}
	
	protected abstract void tick(double d);
}