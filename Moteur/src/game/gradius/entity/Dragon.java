package game.gradius.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.Grid;
import engine.geometry.ISU;
import engine.move.Model;
import game.Game;
import game.gradius.stunt.FollowerStunt;
import game.gradius.stunt.FollowerStunt.MovementState;
import game.gradius.stunt.LeaderStunt;

public class Dragon extends Entity {
	
	private List<Entity> dragon_parts;
	private final static int TICK_DELAY = 30;
	private final static Model model = Game.game().model;

	public Dragon(int nb_segments) {
		super("Dragon");
		dragon_parts = new ArrayList<Entity>();
		DragonHead head = new DragonHead();
		LeaderStunt headStunt = new LeaderStunt(model, head);
		model.add(head, headStunt);
		dragon_parts.add(head);
		LinkedList<MovementState> leaderHistory = headStunt.getHistory();
		for(int i=0; i<nb_segments; i++) {
			DragonBody body = new DragonBody();
			FollowerStunt bodyStunt = new FollowerStunt(model, body, leaderHistory, TICK_DELAY);
			model.add(body, bodyStunt);
	
			dragon_parts.add(body);
			
			leaderHistory = bodyStunt.getMyHistory();
		}
		headStunt.setAngularSpeed(45);
		//headStunt.setLinearSpeed(isu.new Vector(0, 20));
		place(isu.new Coord(0,0));
	}
	
	@Override
	public void place(ISU.Coord center) {
		super.place(center); 
		
		for(Entity part: dragon_parts) {
			part.place(center);
		}
	}
	
	@Override
	public void place(Grid.Position pos) {
		super.place(pos); 
		
		for(Entity part: dragon_parts) {
			part.place(pos);
		}
	}

	@Override
	protected void setBounding() {
		clearBounding();
	}

}