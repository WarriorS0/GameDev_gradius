package game.gradius.stunt;

import java.util.LinkedList;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.move.Model;
import engine.move.Stunt;
import game.Game;

public class FollowerStunt extends Stunt {
	
	public static class MovementState {
		public final ISU.Coord position;
	    public final double orientation;

	    public MovementState(ISU.Coord position, double orientation) {
	        this.position = position;
	        this.orientation = orientation;
	    }
	}
	
	private final LinkedList<MovementState> leaderHistory;
	private final LinkedList<MovementState> myHistory = new LinkedList<>();
	private final int tickDelay;

	public FollowerStunt(Model model, Entity entity, LinkedList<MovementState> leaderHistory, int tickDelay) {
		super(model, entity);
		this.leaderHistory = leaderHistory;
        this.tickDelay = tickDelay;
	}
	
	public LinkedList<MovementState> getMyHistory() {
        return myHistory;
    }

	@Override
	protected void collision(Entity entity) {
		// TODO Auto-generated method stub	
	}

	@Override
	protected void collision(List<Entity> entities) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void tick(double d) {
		myHistory.addLast(new MovementState(entity.center().mkCopy(), entity.orientation()));
		
		if(leaderHistory.size() > tickDelay) {
			MovementState target = leaderHistory.getFirst();
			
			ISU.Vector speedVector = entity.center().mkVectorToward(target.position);
			speedVector.scale(1/d);
			setLinearSpeed(speedVector);
			
			double targetAngleRad = Math.atan2(speedVector.y(), speedVector.x());
			double currentAngleRad = Math.toRadians(entity.orientation());
			double diffAngleRad = targetAngleRad - currentAngleRad;
			diffAngleRad = Math.atan2(Math.sin(diffAngleRad), Math.cos(diffAngleRad));
			double angularSpeedRad = diffAngleRad / d;
			double angularSpeedDeg = Math.toDegrees(angularSpeedRad);
			this.setAngularSpeed(angularSpeedDeg);
			
			leaderHistory.removeFirst();
		} else {
			setLinearSpeed(Game.game().isu.new Vector(0, 0));
			setAngularSpeed(0);
		}
	}

}