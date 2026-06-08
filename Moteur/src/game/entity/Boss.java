// = BOSS =

package game.entity;

import engine.entity.Entity;

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
		this.setBounding();
	}

	// TASK COLLISION

	@Override
	protected void setBounding() {
		super.setBounding();

		// TODO:
		// Le Boss a une forme de "T".
		// Il peut être englobé par l'union de deux rectangles :
		// - un rectangle vertical
		// - un rectangle horizontal
		//
		// Ces rectangles devront dépendre :
		// - du center()
		// - de la taille
		// - de orientation()
	}

}
