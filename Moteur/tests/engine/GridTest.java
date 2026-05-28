package engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import game.Game;

class GridTest {

	private static final int WIDTH = 12;
	private static final int HEIGHT = 10;
	private static final double EPS = 1e-9;

	private Game game;
	private Grid grid;
	private ISU isu;
	private double cmPerCell;

	@BeforeEach
	void setUp() {
		game = new Game(WIDTH, HEIGHT);
		grid = game.grid;
		isu = game.isu;
		cmPerCell = game.cmPerCell;

		assertNotNull(grid);
		assertNotNull(isu);
	}

	private Grid.Position pos(int x, int y) {
		return grid.new Position(x, y);
	}

	private Grid.Vector vec(int x, int y) {
		return grid.new Vector(x, y);
	}

	private Grid.Dimension dim(int x, int y) {
		return grid.new Dimension(x, y);
	}

	private void assertPositionEquals(int expectedX, int expectedY, Grid.Position actual) {
		assertAll(
			() -> assertEquals(expectedX, actual.x()),
			() -> assertEquals(expectedY, actual.y())
		);
	}

	private int wrap(int value, int perimeter) {
		int r = value % perimeter;
		return r < 0 ? r + perimeter : r;
	}

	@Test
	@DisplayName("Grid should keep the dimensions given by Game")
	void gridShouldKeepGameDimensions() {
		assertEquals(WIDTH, grid.width());
		assertEquals(HEIGHT, grid.height());
	}

	@Test
	@DisplayName("Every valid position should have a non-null cell")
	void everyValidPositionShouldHaveCell() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				assertNotNull(grid.cellAt(pos(x, y)), "Missing cell at (" + x + "," + y + ")");
			}
		}
	}

	@Test
	@DisplayName("cellAt should behave according to torus geometry")
	void cellAtShouldNormalizePositionOnTorus() {
		Grid.Cell origin = grid.cellAt(pos(0, 0));

		Grid.Cell wrappedRight = assertDoesNotThrow(() -> grid.cellAt(pos(WIDTH, 0)));
		Grid.Cell wrappedLeft = assertDoesNotThrow(() -> grid.cellAt(pos(-1, 0)));
		Grid.Cell wrappedTop = assertDoesNotThrow(() -> grid.cellAt(pos(0, -1)));
		Grid.Cell wrappedBottom = assertDoesNotThrow(() -> grid.cellAt(pos(0, HEIGHT)));

		assertEquals(origin.position, wrappedRight.position);
		assertPositionEquals(WIDTH - 1, 0, wrappedLeft.position);
		assertPositionEquals(0, HEIGHT - 1, wrappedTop.position);
		assertEquals(origin.position, wrappedBottom.position);
	}

	@Test
	@DisplayName("Dimension.normalize should wrap coordinates on torus")
	void dimensionNormalizeShouldWrapOnTorus() {
		Grid.Dimension d = dim(WIDTH + 3, HEIGHT + 4);

		d.normalize();

		assertEquals(3, d.x());
		assertEquals(4, d.y());
	}

	@Test
	@DisplayName("Dimension.normalize should wrap negative coordinates on torus")
	void dimensionNormalizeShouldWrapNegativeCoordinatesOnTorus() {
		Grid.Dimension d = dim(-1, -2);

		d.normalize();

		assertEquals(WIDTH - 1, d.x());
		assertEquals(HEIGHT - 2, d.y());
	}

	@Test
	@DisplayName("Vector.add should add components and normalize")
	void vectorAddShouldAddAndNormalize() {
		Grid.Vector v = vec(WIDTH - 1, HEIGHT - 1);

		v.add(vec(1, 2));

		assertEquals(0, v.x());
		assertEquals(1, v.y());
	}

	@Test
	@DisplayName("Position.translate should translate and normalize")
	void positionTranslateShouldTranslateAndNormalize() {
		Grid.Position p = pos(WIDTH - 1, HEIGHT - 1);

		p.translate(vec(1, 2));

		assertPositionEquals(0, 1, p);
	}

	@Test
	@DisplayName("moveNorth should wrap from top to bottom on torus")
	void moveNorthShouldWrapFromTopToBottom() {
		Grid.Position p = pos(3, 0);

		p.moveNorth(1);

		assertPositionEquals(3, HEIGHT - 1, p);
	}

	@Test
	@DisplayName("Position.copy should create an independent equal position")
	void positionCopyShouldBeIndependent() {
		Grid.Position p = pos(2, 3);
		Grid.Position copy = p.copy();

		assertEquals(p, copy);
		assertNotSame(p, copy);

		copy.translate(vec(1, 0));

		assertPositionEquals(2, 3, p);
		assertPositionEquals(3, 3, copy);
	}

	@Test
	@DisplayName("Position.distanceTo should use shortest path on torus")
	void distanceShouldUseShortestPathOnTorus() {
		assertEquals(1.0, pos(0, 0).distanceTo(pos(WIDTH - 1, 0)), EPS);
		assertEquals(1.0, pos(0, 0).distanceTo(pos(0, HEIGHT - 1)), EPS);
		assertEquals(Math.sqrt(2.0), pos(0, 0).distanceTo(pos(WIDTH - 1, HEIGHT - 1)), EPS);
	}

	@Test
	@DisplayName("Rotation around a position by 90 degrees should preserve distance")
	void rotationAroundShouldPreserveDistanceForRightAngle() {
		Grid.Position center = pos(5, 5);
		Grid.Position p = pos(6, 5);

		double before = p.distanceTo(center);
		p.rotateAround(center, 90);
		double after = p.distanceTo(center);

		assertEquals(before, after, EPS);
		assertPositionEquals(5, 6, p);
	}

	@Test
	@DisplayName("Cell should add, contain and remove entities")
	void cellShouldAddContainAndRemoveEntity() {
		Grid.Cell cell = grid.cellAt(pos(0, 0));

		assertFalse(cell.contains(null));

		cell.add(null);
		assertTrue(cell.contains(null));

		cell.remove(null);
		assertFalse(cell.contains(null));
	}

	@Test
	@DisplayName("Grid.Dimension to ISU.Dimension should scale by cmPerCell")
	void gridDimensionToISUDimensionShouldScaleByCmPerCell() {
		Grid.Dimension d = dim(2, 3);

		ISU.Dimension isuDim = d.toISUDimension();

		assertNotNull(isuDim);
		assertEquals(2 * cmPerCell, isuDim.x(), EPS);
		assertEquals(3 * cmPerCell, isuDim.y(), EPS);
	}

	@Test
	@DisplayName("Centered Grid position to ISU coordinate should preserve cell spacing")
	void gridPositionToCenteredISUCoordShouldPreserveCellSpacing() {
		ISU.Coord c00 = pos(0, 0).toISUCoordCentered();
		ISU.Coord c10 = pos(1, 0).toISUCoordCentered();
		ISU.Coord c01 = pos(0, 1).toISUCoordCentered();

		assertNotNull(c00);
		assertNotNull(c10);
		assertNotNull(c01);

		assertEquals(cmPerCell, c00.distanceTo(c10), EPS);
		assertEquals(cmPerCell, c00.distanceTo(c01), EPS);
	}

	@Test
	@DisplayName("Position(0,0).toISUCoordCentered should be the origin cell center")
	void gridOriginCenteredCoordShouldRoundTripToOriginPosition() {
		Grid.Position p = pos(0, 0);

		ISU.Coord center = p.toISUCoordCentered();

		assertNotNull(center);
		assertEquals(p, center.toGridPosition());
	}

	@Test
	@DisplayName("Grid position -> centered ISU coord -> Grid position should round-trip")
	void gridToCenteredISUToGridShouldRoundTrip() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				Grid.Position p = pos(x, y);

				ISU.Coord center = p.toISUCoordCentered();

				assertNotNull(center);
				assertEquals(p, center.toGridPosition(), "Round-trip failed at (" + x + "," + y + ")");
			}
		}
	}

	@Test
	@DisplayName("ISU coordinate centers should convert to corresponding Grid positions")
	void isuCoordCentersShouldConvertToGridPositions() {
		assertEquals(pos(0, 0), isu.new Coord(0.0, 0.0).toGridPosition());
		assertEquals(pos(1, 0), isu.new Coord(cmPerCell, 0.0).toGridPosition());
		assertEquals(pos(0, 1), isu.new Coord(0.0, cmPerCell).toGridPosition());
	}

	@Test
	@DisplayName("Negative ISU coordinates should wrap to torus positions")
	void negativeISUCoordShouldWrapToTorusPosition() {
		assertEquals(pos(WIDTH - 1, 0), isu.new Coord(-cmPerCell, 0.0).toGridPosition());
		assertEquals(pos(0, HEIGHT - 1), isu.new Coord(0.0, -cmPerCell).toGridPosition());
	}

	@Test
	@DisplayName("Top-left ISU coord of a cell should be half a cell away from its center")
	void gridPositionToISUCoordShouldRepresentTopLeftCorner() {
		Grid.Position p = pos(0, 0);

		ISU.Coord topLeft = p.toISUCoord();
		ISU.Coord center = p.toISUCoordCentered();

		assertNotNull(topLeft);
		assertNotNull(center);

		double expectedDistance = Math.sqrt(
			(cmPerCell / 2.0) * (cmPerCell / 2.0)
			+ (cmPerCell / 2.0) * (cmPerCell / 2.0)
		);

		assertEquals(expectedDistance, topLeft.distanceTo(center), EPS);
	}

	@Test
	@DisplayName("Random wrapped positions should normalize to the expected cell")
	void wrappedPositionsShouldMatchReferenceModuloFormula() {
		int[][] samples = {
			{0, 0},
			{WIDTH, HEIGHT},
			{WIDTH + 1, HEIGHT + 2},
			{-1, -1},
			{-WIDTH - 3, -HEIGHT - 4}
		};

		for (int[] sample : samples) {
			int x = sample[0];
			int y = sample[1];

			Grid.Position p = pos(x, y);
			p.normalize();

			assertPositionEquals(wrap(x, WIDTH), wrap(y, HEIGHT), p);
		}
	}
}