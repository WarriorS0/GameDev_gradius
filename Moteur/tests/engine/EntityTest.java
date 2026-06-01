package engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.Game;

class EntityTest {

	private static final int WIDTH_NCELL = 12;
	private static final int HEIGHT_NCELL = 10;
	private static final double EPSILON = 1e-9;

	private Game game;
	private Grid grid;
	private ISU isu;
	private Entity entity;

	@BeforeEach
	void init() {
		game = new Game(WIDTH_NCELL, HEIGHT_NCELL);

		this.game = Game.game();
		this.grid = game.grid;
		this.isu = game.isu;

		this.entity = new Entity("test-entity");
		this.entity.setStep(isu.new Dimension(2.0, 2.0));
	}

	@Test
	void setPositionShouldUpdatePositionAndCenter() {
		Grid.Position position = grid.new Position(2, 3);

		entity.setPosition(position);

		assertEquals(
			position.x_ncell,
			entity.position().x_ncell,
			"setPosition should update position.x."
		);

		assertEquals(
			position.y_ncell,
			entity.position().y_ncell,
			"setPosition should update position.y."
		);

		ISU.Coord expectedCenter = position.toISUCoord();

		assertEquals(
			expectedCenter.x(),
			entity.center().x(),
			EPSILON,
			"setPosition should update center.x from position.toISUCoord()."
		);

		assertEquals(
			expectedCenter.y(),
			entity.center().y(),
			EPSILON,
			"setPosition should update center.y from position.toISUCoord()."
		);
	}

	@Test
	void setCoordShouldUpdateCenterAndPosition() {
		ISU.Coord center = grid.new Position(4, 5).toISUCoord();

		entity.setCoord(center);

		assertEquals(
			center.x(),
			entity.center().x(),
			EPSILON,
			"setCoord should update center.x."
		);

		assertEquals(
			center.y(),
			entity.center().y(),
			EPSILON,
			"setCoord should update center.y."
		);

		Grid.Position expectedPosition = center.toGridPosition();

		assertEquals(
			expectedPosition.x_ncell,
			entity.position().x_ncell,
			"setCoord should update position.x from center.toGridPosition()."
		);

		assertEquals(
			expectedPosition.y_ncell,
			entity.position().y_ncell,
			"setCoord should update position.y from center.toGridPosition()."
		);
	}

	@Test
	void turnShouldNormalizePositiveAngles() {
		assertEquals(0, entity.orientation(), "Initial orientation should be 0.");

		entity.turn(90);
		assertEquals(90, entity.orientation(), "turn(90) from 0 should give 90.");

		entity.turn(300);
		assertEquals(30, entity.orientation(), "90 + 300 should be normalized to 30.");
	}

	@Test
	void turnShouldNormalizeNegativeAngles() {
		assertEquals(0, entity.orientation(), "Initial orientation should be 0.");

		entity.turn(-60);
		assertEquals(300, entity.orientation(), "turn(-60) from 0 should give 300.");

		entity.turn(-330);
		assertEquals(330, entity.orientation(), "300 - 330 should be normalized to 330.");
	}

	@Test
	void turnShouldNormalizeAnglesGreaterThan360() {
		assertEquals(0, entity.orientation(), "Initial orientation should be 0.");

		entity.turn(720);
		assertEquals(0, entity.orientation(), "turn(720) should keep orientation at 0.");

		entity.turn(450);
		assertEquals(90, entity.orientation(), "turn(450) should be equivalent to turn(90).");
	}

	@Test
	void moveEastByCentimetersShouldUpdateCenterAndPosition() {
		entity.setCoord(grid.new Position(4, 5).toISUCoord());

		double x0 = entity.center().x();
		double y0 = entity.center().y();

		entity.moveEast(1.5);

		assertEquals(x0 + 1.5, entity.center().x(), EPSILON, "moveEast should increase x.");
		assertEquals(y0, entity.center().y(), EPSILON, "moveEast should not change y.");

		Grid.Position expectedPosition = entity.center().toGridPosition();

		assertEquals(expectedPosition.x_ncell, entity.position().x_ncell);
		assertEquals(expectedPosition.y_ncell, entity.position().y_ncell);
	}

	@Test
	void moveWestByCentimetersShouldUpdateCenterAndPosition() {
		entity.setCoord(grid.new Position(4, 5).toISUCoord());

		double x0 = entity.center().x();
		double y0 = entity.center().y();

		entity.moveWest(1.5);

		assertEquals(x0 - 1.5, entity.center().x(), EPSILON, "moveWest should decrease x.");
		assertEquals(y0, entity.center().y(), EPSILON, "moveWest should not change y.");

		Grid.Position expectedPosition = entity.center().toGridPosition();

		assertEquals(expectedPosition.x_ncell, entity.position().x_ncell);
		assertEquals(expectedPosition.y_ncell, entity.position().y_ncell);
	}

	@Test
	void moveSouthByCentimetersShouldUpdateCenterAndPosition() {
		entity.setCoord(grid.new Position(4, 5).toISUCoord());

		double x0 = entity.center().x();
		double y0 = entity.center().y();

		entity.moveSouth(1.5);

		assertEquals(x0, entity.center().x(), EPSILON, "moveSouth should not change x.");
		assertEquals(y0 + 1.5, entity.center().y(), EPSILON, "moveSouth should increase y.");

		Grid.Position expectedPosition = entity.center().toGridPosition();

		assertEquals(expectedPosition.x_ncell, entity.position().x_ncell);
		assertEquals(expectedPosition.y_ncell, entity.position().y_ncell);
	}

	@Test
	void moveNorthByCentimetersShouldUpdateCenterAndPosition() {
		entity.setCoord(grid.new Position(4, 5).toISUCoord());

		double x0 = entity.center().x();
		double y0 = entity.center().y();

		entity.moveNorth(1.5);

		assertEquals(x0, entity.center().x(), EPSILON, "moveNorth should not change x.");
		assertEquals(y0 - 1.5, entity.center().y(), EPSILON, "moveNorth should decrease y.");

		Grid.Position expectedPosition = entity.center().toGridPosition();

		assertEquals(expectedPosition.x_ncell, entity.position().x_ncell);
		assertEquals(expectedPosition.y_ncell, entity.position().y_ncell);
	}

	@Test
	void moveByStepsShouldUseStepXForEastAndWest() {
		entity.setCoord(grid.new Position(4, 5).toISUCoord());
		entity.setStep(isu.new Dimension(2.0, 3.0));

		double x0 = entity.center().x();
		double y0 = entity.center().y();

		entity.moveEast(4);

		assertEquals(x0 + 8.0, entity.center().x(), EPSILON, "moveEast(4) should add 4 * step.x().");
		assertEquals(y0, entity.center().y(), EPSILON, "moveEast(4) should not change y.");

		entity.moveWest(3);

		assertEquals(x0 + 2.0, entity.center().x(), EPSILON, "moveWest(3) should remove 3 * step.x().");
		assertEquals(y0, entity.center().y(), EPSILON, "moveWest(3) should not change y.");
	}

	@Test
	void moveByStepsShouldUseStepYForNorthAndSouth() {
		entity.setCoord(grid.new Position(4, 5).toISUCoord());
		entity.setStep(isu.new Dimension(2.0, 3.0));

		double x0 = entity.center().x();
		double y0 = entity.center().y();

		entity.moveSouth(4);

		assertEquals(x0, entity.center().x(), EPSILON, "moveSouth(4) should not change x.");
		assertEquals(y0 + 12.0, entity.center().y(), EPSILON, "moveSouth(4) should add 4 * step.y().");

		entity.moveNorth(3);

		assertEquals(x0, entity.center().x(), EPSILON, "moveNorth(3) should not change x.");
		assertEquals(y0 + 3.0, entity.center().y(), EPSILON, "moveNorth(3) should remove 3 * step.y().");
	}

	@Test
	void translateGridVectorShouldUpdatePositionAndCenter() {
		entity.setPosition(grid.new Position(2, 3));

		entity.translate(grid.new Vector(4, -1));

		assertEquals(6, entity.position().x_ncell, "Grid translation should update position.x.");
		assertEquals(2, entity.position().y_ncell, "Grid translation should update position.y.");

		ISU.Coord expectedCenter = entity.position().toISUCoord();

		assertEquals(
			expectedCenter.x(),
			entity.center().x(),
			EPSILON,
			"After grid translation, center.x should be synchronized."
		);

		assertEquals(
			expectedCenter.y(),
			entity.center().y(),
			EPSILON,
			"After grid translation, center.y should be synchronized."
		);
	}
}