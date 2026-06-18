package game.entity;

import java.util.ArrayList;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.ISU;

public class Dragon extends Entity {
	
	private List<Entity> dragon_parts;
	private final static double L = 3.0;

	protected Dragon(int nb_segments) {
		super("Dragon");
		dragon_parts = new ArrayList<Entity>();
		dragon_parts.add(new DragonHead());
		for(int i=0; i<nb_segments; i++) {
			dragon_parts.add(new DragonBody());
		}
		place(isu.new Coord(0,0));
	}
	
	@Override
	public void place(ISU.Coord center) {
		super.place(isu.new Coord(0,0)); 
		
		dragon_parts.get(0).place(center);
		
		
		for(int i = 1; i < dragon_parts.size(); i++) {
			Entity leader = dragon_parts.get(i - 1);
			ISU.Coord spawnCoord = isu.new Coord(leader.center().x() - L, leader.center().y());
			
			dragon_parts.get(i).place(spawnCoord);
		}
	}

	@Override
	protected void setBounding() {
		clearBounding();
	}

}
