package game.gradius.graphics;

public class MediumTile extends Tile {

	public MediumTile(int max_x,int floor) {
		super(max_x);
		super.set_entity_y(floor);
	}

	@Override
	protected void setBounding() {
		// TODO Auto-generated method stub
		
	}

}
