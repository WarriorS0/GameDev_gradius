package game;

import engine.Grid;
import engine.ISU;
import engine.Picture;

import java.io.PrintStream;

public class Game {

	// CONSTANT

	// public final car la taille du monde ne change pas

	public final boolean torusOnXaxis = true; // vrai si l'axe X est une boucle fermée
	public final boolean torusOnYaxis = true; // vrai si l'axe Y est une boucle fermée

	public final double cmPerCell = 3.7; // échelle qui relie l'unité ncell à cm
	public final int pixelPerCm = 2; // échelle qui relie l'unité pixel à cm

	// FIELDS

	public final int width_ncell; // largeur du monde en nombre de cellules
	public final int height_ncell; // hauteur du monde en nombre de celluls

	public final double width_cm; // largeur du monde en cm
	public final double height_cm; // hauteur du monde en cm

	public final Grid grid; // permet la création de coordonnées en unités ncell
	public final ISU isu; // permet la création de coordonnées en unités cm
	public final Picture pict; // permet la création de coordonnées en unités pixel, ne sera
	// utilisé qu'à
	// partir de Task2

	// CONSTRUCTORS

	public Game(int w_ncell, int h_ncell) {
		this.width_ncell = w_ncell;
		this.height_ncell = h_ncell;

		this.width_cm = this.cmPerCell * this.width_ncell;
		this.height_cm = this.cmPerCell * this.height_ncell;

		assert (width_ncell * cmPerCell == width_cm);
		assert (height_ncell * cmPerCell == height_cm);

		this.grid = new Grid(this);
		this.isu = new ISU(this);
		this.pict = new Picture();
		Game.game = this;

	}

	public Game(double w_cm, double h_cm) {

		// erreur des arrondies => epsilon
		double w_cells = w_cm / cmPerCell;
		double h_cells = h_cm / cmPerCell;

		double epsilon = 1e-9;

		if (Math.abs(w_cells - Math.round(w_cells)) > epsilon || Math.abs(h_cells - Math.round(h_cells)) > epsilon) {
			throw new IllegalArgumentException(
					"La taille doit être un multiple de la taille d'une cellule qui vaut : " + cmPerCell);
		}

		this.width_ncell = (int) Math.round(w_cells);
		this.height_ncell = (int) Math.round(h_cells);

		this.height_cm = h_cm;
		this.width_cm = w_cm;


		assert (width_ncell * cmPerCell == width_cm);
		assert (height_ncell * cmPerCell == height_cm);

		this.grid = new Grid(this);
		this.isu = new ISU(this);
		this.pict = new Picture();
		Game.game = this;

	}

	// GETTER

	private static Game game;

	static Game game() {
		return game;
	}

	// SHOW

	public void show(PrintStream ps) {
		ps.printf(this.toString());
		ps.printf("\n width in cm = %f \n height in cm = %f\n width in cell = %d \n height in cell = %d", this.width_cm,
				this.height_cm, this.width_ncell, this.height_ncell);
	}
}
