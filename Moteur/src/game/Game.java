package game;

import engine.Grid;
import engine.ISU;

import java.io.PrintStream;

public class Game {

  // CONSTANT

   boolean torusOnXaxis = true; // vrai si l'axe X est une boucle fermée
   boolean torusOnYaxis = true; // vrai si l'axe Y est une boucle fermée

   double cmPerCell = 3.7; // échelle qui relie l'unité ncell à cm
   int pixelPerCm = 2; // échelle qui relie l'unité pixel à cm

  // FIELDS

   int width_ncell; // largeur du monde en nombre de cellules
   int height_ncell; // hauteur du monde en nombre de celluls

   double width_cm; // largeur du monde en cm
   double height_cm; // hauteur du monde en cm

   private Grid grid; // permet la création de coordonnées en unités ncell
   private ISU isu; // permet la création de coordonnées en unités cm
   //Picture pict; // permet la création de coordonnées en unités pixel, ne sera utilisé qu'à
                             // partir de Task2

  // CONSTRUCTORS

   public Game(int w_ncell, int h_ncell) { 
	   this.width_ncell = w_ncell;
	   this.height_ncell = h_ncell;
	   
	   this.width_cm = this.cmPerCell * this.width_ncell;
	   this.height_cm = this.cmPerCell * this.height_ncell;
	   
	   assert( width_ncell * cmPerCell == width_cm );
	   assert( height_ncell * cmPerCell == height_cm );
	   
	   this.grid = new Grid(this);
	   this.isu = new ISU(this);
	   //this.pict = new Picture();
	   this.game = this;

   }

   public Game(double w_cm, double h_cm) {
	   
	   this.height_cm = h_cm;
	   this.width_cm = w_cm;
	   
	   this.width_ncell = (int) (w_cm % cmPerCell) + 1 ;
	   this.height_ncell = (int) (h_cm % cmPerCell) + 1 ;
	   
	   assert( width_ncell * cmPerCell == width_cm );
	   assert( height_ncell * cmPerCell == height_cm );
	   
	   this.grid = new Grid(this);
	   this.isu = new ISU(this);
	   //this.pict = new Picture();
	   this.game = this;

   }
   

  // GETTER

   Game game;

   Game game() { return game; }

  // SHOW

   void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }
}
