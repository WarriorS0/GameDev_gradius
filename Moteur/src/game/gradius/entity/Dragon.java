package game.gradius.entity;

import java.util.ArrayList;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.ISU;
import game.Game;

public class Dragon extends Entity {
	
	private List<Entity> dragon_parts;
	private final static int TICK_DELAY = 10;

	protected Dragon(int nb_segments) {
		super("Dragon");
		dragon_parts = new ArrayList<Entity>();
		//DragonHead head = new DragonHead();
		dragon_parts.add(new DragonHead());
		for(int i=0; i<nb_segments; i++) {
			dragon_parts.add(new DragonBody());
		}
		place(isu.new Coord(0,0));
	}
	
	@Override
	public void place(ISU.Coord center) {
		super.place(isu.new Coord(0,0)); 
		
		for(Entity part: dragon_parts) {
			part.place(center);
		}
	}

	@Override
	protected void setBounding() {
		clearBounding();
	}

}