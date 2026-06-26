package game.gradius.graphics;

import java.util.Random;

import engine.gal.arguments.Category;
import engine.graphics.View;
import engine.move.Model;
import game.Game;

/**
 * Generates the tunnel terrain: a random sequence of {@link Tile} obstacles laid
 * edge to edge across the whole world width, against both the top and bottom
 * edges (so the playfield is a horizontal tunnel).
 *
 * Each tile's height comes from its kind (proportional to the sprite, never
 * stretched), so a tile is glued flush to its world edge and extends inward by
 * its own height. Tiles never cross the toric seam in Y.
 *
 * Built once at initialization; the world looping on X, the player loops over
 * the same terrain (dynamic recycling can be added later).
 */
public class TerrainGenerator {

	private static final TerrainKind[] KINDS = { TerrainKind.BIG, TerrainKind.MEDIUM_1, TerrainKind.MEDIUM_2,
			TerrainKind.MEDIUM_3, TerrainKind.FLAT };

	private final Model model;
	private final View view;
	private final Random random;

	public TerrainGenerator(Model model, View view) {
		this(model, view, new Random());
	}

	public TerrainGenerator(Model model, View view, long seed) {
		this(model, view, new Random(seed));
	}

	private TerrainGenerator(Model model, View view, Random random) {
		this.model = model;
		this.view = view;
		this.random = random;
	}

	/**
	 * Builds the whole tunnel: a random row of tiles along the top edge and one
	 * along the bottom edge, each covering the full world width.
	 */
	public void generate() {
		// Terrain ignores terrain: decor tiles never test collisions against each
		// other, only against the gameplay entities (ship, projectiles).
		Category.setInteraction(Category.Obstacle, Category.Obstacle, false);

		Game game = Game.game();
		fillRow(game.width_cm, game.height_cm, true); // top
		fillRow(game.width_cm, game.height_cm, false); // bottom
	}

	/**
	 * Lays a random sequence of tiles left to right until the world width is
	 * covered, glued to one edge.
	 *
	 * @param worldW world width to cover, in cm
	 * @param worldH world height, in cm
	 * @param topRow true to glue to the top edge, false for the bottom edge
	 */
	private void fillRow(double worldW, double worldH, boolean topRow) {
		double cursor = 0.0;

		while (cursor < worldW) {
			TerrainKind kind = KINDS[random.nextInt(KINDS.length)];

			Tile tile = new Tile(kind, topRow);

			double h = kind.height_cm();
			double centerX = cursor + kind.width_cm / 2.0;
			// Glue flush to the edge: the tile occupies [0, h] (top) or
			// [worldH - h, worldH] (bottom), fully on its own side of the torus.
			double centerY = topRow ? (h / 2.0) : (worldH - h / 2.0);

			tile.place(Game.game().isu.new Coord(centerX, centerY));

			model.add(tile);
			view.add(new TileAvatar(tile, model.viewPort()));

			cursor += kind.width_cm;
		}
	}
}