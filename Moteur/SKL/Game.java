// == GAME ==

 class Game {

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

   Grid grid; // permet la création de coordonnées en unités ncell
   ISU isu; // permet la création de coordonnées en unités cm
   Picture pict; // permet la création de coordonnées en unités pixel, ne sera utilisé qu'à
                             // partir de Task2

  // CONSTRUCTORS

   Game(int w_ncell, int h_ncell) { throw new UnsupportedOperationException("Unimplemented method"); }

   Game(double w_cm, double h_cm) { throw new UnsupportedOperationException("Unimplemented method"); }

  // GETTER

   Game game;

   Game game() { return null; }

  // SHOW

   void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }
}
