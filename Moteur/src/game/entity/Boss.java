package game.entity;

import engine.entity.Entity;
import engine.geometry.ISU;
import engine.shape.Rect;
import game.Game;

/**
 * @implNote Le Boss a la forme d'un `t`
 * @implNote il occupe 1 cellule au dessus de son centre,
 *           2 au dessous de son centre,
 *           et 2 cellules à droite de son centre.
 * @implNote Le Boss a donc une dimension (x=3Cell,y=4Cell)
 * @implNote Cette forme a été choisie pour pouvoir tester les rotations.
 */
public class Boss extends Entity {

	public Boss() {
		super("Boss");
		place(grid.new Position(20, 20));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		double c = Game.game().cmPerCell;
		double angle = orientation();

		Rect vertical = new Rect(
			centerFromLocalOffset(0, 0.5 * c),
			isu.new Dimension(1 * c, 4 * c),
			angle
		);

		Rect horizontal = new Rect(
			centerFromLocalOffset(1 * c, 0),
			isu.new Dimension(3 * c, 1 * c),
			angle
		);

		addBounding(vertical);
		addBounding(horizontal);
	}

	private ISU.Coord centerFromLocalOffset(double dx, double dy) {
		double angle = Math.toRadians(orientation());

		double rotatedDx = dx * Math.cos(angle) - dy * Math.sin(angle);
		double rotatedDy = dx * Math.sin(angle) + dy * Math.cos(angle);

		return isu.new Coord(
			center().x() + rotatedDx,
			center().y() + rotatedDy
		);
	}
}