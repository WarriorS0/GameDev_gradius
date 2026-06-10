// = BOSS =

package game.entity;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.shape.Rect;
import game.Game;

/**
//  X
//  *XX   |--X--X--*--|
//  X
//  X
*/

/**
 * @implNote Le Boss a la forme d'un `t`
 * @implNote il occupe 1 cellule au dessus de son
 *           centre, 2 au dessous de son centre, et
 *           2 cellules à droite de son centre.
 * @implNote Le Boss a donc une dimension (x=3Cell,y=4Cell)
 * @implNote Cette forme a été choisie pour pouvoir tester les rotations.
 */
public class Boss extends Entity {

	// CONSTRUCTOR

	public Boss() {
		super("Boss");
		super.setPosition(super.grid.new Position(20,20));
		this.setBounding();
	}

	// TASK COLLISION

	@Override
	protected void setBounding() {
		super.setBounding();

		double c = Game.game().cmPerCell;
		int angle = super.orientation();

		// Barre verticale :
		Rect vertical = new Rect(
			centerFromLocalOffset(0, 0.5 * c),
			super.isu.new Dimension(1 * c, 4 * c),
			angle
		);
		super.addBounding(vertical);

		// Barre horizontale :
		Rect horizontal = new Rect(
			centerFromLocalOffset(1 * c, 0),
			super.isu.new Dimension(3 * c, 1 * c),
			angle
		);
		super.addBounding(horizontal);
	}
	
	private ISU.Coord centerFromLocalOffset(double dx, double dy) {
		double angle = Math.toRadians(super.orientation());

		double rotatedDx = dx * Math.cos(angle) - dy * Math.sin(angle);
		double rotatedDy = dx * Math.sin(angle) + dy * Math.cos(angle);

		return super.isu.new Coord(
			super.center().x() + rotatedDx,
			super.center().y() + rotatedDy
		);
	}

}
