package game.gradius.graphics;

public class FlatTile extends Tile  {
	public FlatTile(int max_x,int floor) {
		super(max_x);
		super.set_entity_y(floor);
	}

	@Override
	protected void setBounding() {
		// TODO Auto-generated method stub
		
	}
}
