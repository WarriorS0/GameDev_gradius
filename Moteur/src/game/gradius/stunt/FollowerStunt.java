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
	
	private final Entity leaderEntity; 
	private final LinkedList<MovementState> leaderHistory;
	private final LinkedList<MovementState> myHistory = new LinkedList<>();
	private final int tickDelay;
	private int deathCountdown = -1;  

	public FollowerStunt(Model model, Entity entity, Entity leaderEntity, LinkedList<MovementState> leaderHistory, int tickDelay) {
		super(model, entity);
		this.leaderEntity = leaderEntity;
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
		// Provoque la mort en chaîne de chaque partie du dragon (selon l'état du leader)
		if (leaderEntity.dead() && deathCountdown < 0) {
			deathCountdown = tickDelay;
		}
 
		if (deathCountdown >= 0) {
			setLinearSpeed(Game.game().isu.new Vector(0, 0));
			myHistory.addLast(new MovementState(entity.center().mkCopy(), entity.orientation()));
			if (deathCountdown == 0) {
				entity.kill();
				return;
			}
			deathCountdown--;
			return;
		}
		
		// Mouvement suivant le leader
		myHistory.addLast(new MovementState(entity.center().mkCopy(), entity.orientation()));
		
		if(leaderHistory.size() > tickDelay) {
			MovementState target = leaderHistory.getFirst();
	        
	        ISU.Vector speedVector = entity.center().mkVectorToward(target.position);
	        speedVector.scale(1/d);
	        setLinearSpeed(speedVector);
	        
	        entity.forceOrientation(target.orientation);
	        
	        leaderHistory.removeFirst();
		} else {
			setLinearSpeed(Game.game().isu.new Vector(0, 0));
			setAngularSpeed(0);
		    entity.forceOrientation(entity.orientation());
		}
	}

}