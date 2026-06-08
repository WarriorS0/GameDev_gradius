package engine.entity;

import engine.shape.Rect;
import game.Game;

class Obstacle  extends Entity{

  // CONSTRUCTOR

   Obstacle(int x_ncell, int y_ncell){ 
	   super("Obstacle");
	   super.setPosition(super.grid.new Position(x_ncell,y_ncell));
	   super.setSize(super.isu.new Dimension(Game.game().cmPerCell,Game.game().cmPerCell ));
	   super.addBounding(new Rect(super.center(), super.size));
	   
   }

  // === Task COLLISION ===
   void setBounding(){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `setBounding`"); }

}
