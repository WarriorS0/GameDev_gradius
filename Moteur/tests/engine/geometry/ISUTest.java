package engine.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import game.Game;

class ISUTest {

	private static final int WIDTH_NCELL = 12;
	private static final int HEIGHT_NCELL = 10;
	private static final double EPS = 1e-9;

	private Game game;
	private Grid grid;
	private ISU isu;

	private double cmPerCell;
	private double worldWidthCm;
	private double worldHeightCm;

	@BeforeEach
	void setUp() {
		game = new Game(WIDTH_NCELL, HEIGHT_NCELL);
		grid = game.grid;
		isu = game.isu;

		cmPerCell = game.cmPerCell;
		worldWidthCm = game.width_cm;
		worldHeightCm = game.height_cm;

		assertNotNull(grid);
		assertNotNull(isu);
		assertTrue(cmPerCell > 0);
		assertEquals(WIDTH_NCELL * cmPerCell, worldWidthCm, EPS);
		assertEquals(HEIGHT_NCELL * cmPerCell, worldHeightCm, EPS);
	}

	private double cell(int ncell) {
		return ncell * cmPerCell;
	}

	private double wrapCm(double valueCm, double perimeterCm) {
		double r = valueCm % perimeterCm;
		return r < 0 ? r + perimeterCm : r;
	}

	private void assertCoordEquals(double expectedXcm, double expectedYcm, ISU.Coord actual) {
		assertEquals(expectedXcm, actual.x(), EPS);
		assertEquals(expectedYcm, actual.y(), EPS);
	}

	private void assertVectorEquals(double expectedXcm, double expectedYcm, ISU.Vector actual) {
		assertEquals(expectedXcm, actual.x(), EPS);
		assertEquals(expectedYcm, actual.y(), EPS);
	}

	// ============================================================
	// ISU : toutes les coordonnées sont en cm
	// ============================================================

	@Test
	@DisplayName("Coord should store coordinates already inside the world, in cm")
	void coordShouldStoreCoordinatesInsideWorldInCm() {
		ISU.Coord c = isu.new Coord(cell(2), cell(3));

		assertCoordEquals(cell(2), cell(3), c);
	}

	@Test
	@DisplayName("Coord should normalize coordinates using the world perimeter in cm")
	void coordShouldNormalizeCoordinatesWithCmPerimeter() {
		ISU.Coord c = isu.new Coord(worldWidthCm + cell(2), -cell(3));

		assertCoordEquals(cell(2), worldHeightCm - cell(3), c);
	}

	@Test
	@DisplayName("setxy should normalize coordinates using cm, not cells")
	void setxyShouldNormalizeCoordinatesWithCmPerimeter() {
		ISU.Coord c = isu.new Coord(0.0, 0.0);

		c.setxy(worldWidthCm + cell(1), worldHeightCm + cell(2));

		assertCoordEquals(cell(1), cell(2), c);
	}

	@Test
	@DisplayName("Dimension should normalize values using cm, not cells")
	void dimensionShouldNormalizeUsingCmPerimeter() {
		ISU.Dimension d = isu.new Dimension(worldWidthCm + cell(4), -cell(2));

		assertEquals(cell(4), d.x(), EPS);
		assertEquals(worldHeightCm - cell(2), d.y(), EPS);
	}

	@Test
	@DisplayName("Coord.translate should add a vector in cm then normalize in cm")
	void translateShouldMoveCoordAndNormalizeResultWithCmUnits() {
		ISU.Coord c = isu.new Coord(worldWidthCm - cell(1), worldHeightCm - cell(1));
		ISU.Vector v = isu.new Vector(cell(2), cell(3));

		c.translate(v);

		assertCoordEquals(cell(1), cell(2), c);
	}

	@Test
	@DisplayName("mkTranslated should return a new coord without changing the original")
	void mkTranslatedShouldReturnNewCoordWithoutChangingOriginal() {
		ISU.Coord original = isu.new Coord(cell(1), cell(2));
		ISU.Vector v = isu.new Vector(cell(3), cell(4));

		ISU.Coord result = original.mkTranslated(v);

		assertCoordEquals(cell(4), cell(6), result);
		assertCoordEquals(cell(1), cell(2), original);
		assertNotSame(original, result);
	}

	@Test
	@DisplayName("mkCopy should return an independent equal coord")
	void mkCopyShouldReturnIndependentEqualCoord() {
		ISU.Coord original = isu.new Coord(cell(1), cell(2));

		ISU.Coord copy = original.mkCopy();

		assertNotSame(original, copy);
		assertEquals(original, copy);

		copy.translate(isu.new Vector(cell(1), cell(1)));

		assertCoordEquals(cell(1), cell(2), original);
		assertCoordEquals(cell(2), cell(3), copy);
	}

	// ============================================================
	// VECTOR
	// ============================================================

	@Test
	@DisplayName("Vector should store components already inside the world, in cm")
	void vectorShouldStoreComponentsInsideWorldInCm() {
		ISU.Vector v = isu.new Vector(cell(2), cell(3));

		assertVectorEquals(cell(2), cell(3), v);
	}

	@Test
	@DisplayName("Vector should normalize components using the world perimeter in cm")
	void vectorShouldNormalizeWithCmPerimeter() {
		ISU.Vector v = isu.new Vector(worldWidthCm + cell(2), -cell(3));

		assertVectorEquals(cell(2), worldHeightCm - cell(3), v);
	}

	@Test
	@DisplayName("Vector.add should add cm components then normalize in cm")
	void addShouldAddVectorsAndNormalizeResultWithCmUnits() {
		ISU.Vector v = isu.new Vector(worldWidthCm - cell(1), worldHeightCm - cell(1));
		ISU.Vector other = isu.new Vector(cell(1), cell(2));

		v.add(other);

		assertVectorEquals(0.0, cell(1), v);
	}

	@Test
	@DisplayName("scale should multiply a vector in cm then normalize in cm")
	void scaleShouldMultiplyVectorAndNormalizeResultWithCmUnits() {
		ISU.Vector v = isu.new Vector(cell(3), 0.0);

		v.scale(4.0);

		assertVectorEquals(0.0, 0.0, v);
	}

	@Test
	@DisplayName("scale(x,y) should multiply each axis in cm")
	void scaleXYShouldMultiplyEachAxisWithCmUnits() {
		ISU.Vector v = isu.new Vector(cell(2), cell(3));

		v.scale(2.0, 3.0);

		assertVectorEquals(cell(4), cell(9), v);
	}

	// ============================================================
	// DOT / NORM / UNITY
	// ============================================================

	@Test
	@DisplayName("dot should return the scalar product in cm²")
	void dotShouldReturnScalarProductInSquareCentimeters() {
		ISU.Vector u = isu.new Vector(cmPerCell, 0.0);
		ISU.Vector v = isu.new Vector(0.0, cmPerCell);
		ISU.Vector w = isu.new Vector(3.0 * cmPerCell, 4.0 * cmPerCell);

		assertEquals(0.0, u.dot(v), EPS);
		assertEquals(25.0 * cmPerCell * cmPerCell, w.dot(w), EPS);
	}

	@Test
	@DisplayName("norm should return the Euclidean norm in cm")
	void normShouldReturnEuclideanNormInCentimeters() {
		ISU.Vector v = isu.new Vector(3.0 * cmPerCell, 4.0 * cmPerCell);

		assertEquals(5.0 * cmPerCell, v.norm(), EPS);
	}

	@Test
	@DisplayName("unity should make vector norm equal to one cm-unit")
	void unityShouldMakeVectorNormEqualToOne() {
		ISU.Vector v = isu.new Vector(3.0, 4.0);

		v.unity();

		assertEquals(1.0, v.norm(), EPS);
		assertVectorEquals(0.6, 0.8, v);
	}

	@Test
	@DisplayName("unity should reject zero vector")
	void unityShouldRejectZeroVector() {
		ISU.Vector v = isu.new Vector(0.0, 0.0);

		assertThrows(IllegalStateException.class, v::unity);
	}

	// ============================================================
	// ROTATION
	// ============================================================

	@Test
	@DisplayName("Coord.rotation(90) should rotate around origin in cm")
	void coordRotationAroundOriginShouldRotatePointBy90DegreesInCm() {
		ISU.Coord c = isu.new Coord(cmPerCell, 0.0);

		c.rotation(90);

		assertCoordEquals(0.0, cmPerCell, c);
	}

	@Test
	@DisplayName("Coord.rotation(180) should rotate around origin then normalize negative cm coordinates")
	void coordRotationAroundOriginShouldRotatePointBy180DegreesAndWrap() {
		ISU.Coord c = isu.new Coord(cmPerCell, 0.0);

		c.rotation(180);

		assertCoordEquals(worldWidthCm - cmPerCell, 0.0, c);
	}

	@Test
	@DisplayName("Coord.rotateAround should rotate around a center in cm")
	void coordRotationAroundCenterShouldRotatePointInCm() {
		ISU.Coord center = isu.new Coord(cell(1), cell(1));
		ISU.Coord point = isu.new Coord(cell(2), cell(1));

		point.rotateAround(center, 90);

		assertCoordEquals(cell(1), cell(2), point);
	}

	@Test
	@DisplayName("Coord.rotateAround should preserve distance to center when no wrapping ambiguity occurs")
	void coordRotationAroundCenterShouldPreserveDistanceToCenter() {
		ISU.Coord center = isu.new Coord(cell(2), cell(2));
		ISU.Coord point = isu.new Coord(cell(4), cell(3));
		double before = point.distanceTo(center);

		point.rotateAround(center, 30);

		double after = point.distanceTo(center);

		assertEquals(before, after, EPS);
	}

	@Test
	@DisplayName("Vector.turn(90) should rotate the vector in cm")
	void vectorTurnShouldRotateVectorBy90DegreesInCm() {
		ISU.Vector v = isu.new Vector(cmPerCell, 0.0);

		v.turn(90);

		assertVectorEquals(0.0, cmPerCell, v);
	}

	@Test
	@DisplayName("Vector.turn(180) should rotate then normalize negative cm components")
	void vectorTurnShouldRotateVectorBy180DegreesAndWrap() {
		ISU.Vector v = isu.new Vector(cmPerCell, 0.0);

		v.turn(180);

		assertVectorEquals(worldWidthCm - cmPerCell, 0.0, v);
	}

	@Test
	@DisplayName("Vector.turn should preserve norm when rotated vector stays in canonical positive range")
	void vectorTurnShouldPreserveNormWhenNoWrapOccurs() {
		ISU.Vector v = isu.new Vector(3.0, 4.0);
		double before = v.norm();

		v.turn(30);

		double after = v.norm();

		assertEquals(before, after, EPS);
	}

	// ============================================================
	// DISTANCE
	// ============================================================

	@Test
	@DisplayName("distanceTo should return Euclidean distance in cm")
	void distanceToShouldReturnEuclideanDistanceInCentimeters() {
		ISU.Coord a = isu.new Coord(0.0, 0.0);
		ISU.Coord b = isu.new Coord(3.0 * cmPerCell, 4.0 * cmPerCell);

		assertEquals(5.0 * cmPerCell, a.distanceTo(b), EPS);
	}

	@Test
	@DisplayName("distanceTo should use shortest torus distance in cm")
	void distanceToShouldUseShortestDistanceOnTorusInCentimeters() {
		ISU.Coord a = isu.new Coord(worldWidthCm - cmPerCell, 0.0);
		ISU.Coord b = isu.new Coord(cmPerCell, 0.0);

		assertEquals(2.0 * cmPerCell, a.distanceTo(b), EPS);
	}

	// ============================================================
	// FACTORIES
	// ============================================================

	@Test
	@DisplayName("mkVectorToward should return a vector from source to target in cm")
	void mkVectorTowardShouldReturnVectorFromSourceToTargetInCm() {
		ISU.Coord source = isu.new Coord(cell(1), cell(1));
		ISU.Coord target = isu.new Coord(cell(4), cell(5));

		ISU.Vector v = source.mkVectorToward(target);

		assertVectorEquals(cell(3), cell(4), v);
	}

	@Test
	@DisplayName("mkVectorToward should canonicalize the vector on a torus using cm")
	void mkVectorTowardShouldReturnCanonicalVectorWhenWorldIsToricInCm() {
		ISU.Coord source = isu.new Coord(worldWidthCm - cmPerCell, 0.0);
		ISU.Coord target = isu.new Coord(cmPerCell, 0.0);

		ISU.Vector v = source.mkVectorToward(target);

		assertVectorEquals(2.0 * cmPerCell, 0.0, v);
	}

	@Test
	@DisplayName("mkVector should create an equivalent vector in cm")
	void mkVectorShouldCreateEquivalentVector() {
		ISU.Coord c = isu.new Coord(cell(3), cell(4));

		ISU.Vector v = c.mkVector();

		assertVectorEquals(cell(3), cell(4), v);
	}

	@Test
	@DisplayName("mkScaledVector should create a scaled vector without changing the source")
	void mkScaledVectorShouldCreateScaledVectorWithoutChangingSource() {
		ISU.Coord c = isu.new Coord(cell(2), cell(3));

		ISU.Vector v = c.mkScaledVector(2.0);

		assertVectorEquals(cell(4), cell(6), v);
		assertCoordEquals(cell(2), cell(3), c);
	}

	@Test
	@DisplayName("mkScaledVector(x,y) should apply different factors in cm")
	void mkScaledVectorXYShouldApplyDifferentFactors() {
		ISU.Coord c = isu.new Coord(cell(2), cell(3));

		ISU.Vector v = c.mkScaledVector(2.0, 3.0);

		assertVectorEquals(cell(4), cell(9), v);
	}

	// ============================================================
	// CONVERSION ISU -> GRID
	// ISU = cm, Grid = cells
	// ============================================================

	@Test
	@DisplayName("toGridPosition should divide cm coordinates by cmPerCell")
	void toGridPositionShouldConvertCmToCellCoordinates() {
		assertEquals(grid.new Position(0, 0), isu.new Coord(0.0, 0.0).toGridPosition());
		assertEquals(grid.new Position(1, 0), isu.new Coord(cmPerCell, 0.0).toGridPosition());
		assertEquals(grid.new Position(0, 1), isu.new Coord(0.0, cmPerCell).toGridPosition());
		assertEquals(grid.new Position(2, 3), isu.new Coord(cell(2), cell(3)).toGridPosition());
	}

	@Test
	@DisplayName("Negative ISU cm coordinates should wrap before conversion to grid cells")
	void negativeCmCoordinatesShouldWrapBeforeConvertingToGridPosition() {
		assertEquals(grid.new Position(WIDTH_NCELL - 1, 0), isu.new Coord(-cmPerCell, 0.0).toGridPosition());
		assertEquals(grid.new Position(0, HEIGHT_NCELL - 1), isu.new Coord(0.0, -cmPerCell + EPS).toGridPosition());
	}

	// ============================================================
	// EQUALITY
	// ============================================================

	@Test
	@DisplayName("Coords with same canonical cm coordinates should be equal")
	void coordsWithSameCanonicalCoordinatesShouldBeEqual() {
		ISU.Coord a = isu.new Coord(worldWidthCm + cell(2), 0.0);
		ISU.Coord b = isu.new Coord(cell(2), 0.0);

		assertEquals(a, b);
	}

	@Test
	@DisplayName("Vectors with same canonical cm coordinates should be equal")
	void vectorsWithSameCanonicalCoordinatesShouldBeEqual() {
		ISU.Vector a = isu.new Vector(worldWidthCm + cell(2), 0.0);
		ISU.Vector b = isu.new Vector(cell(2), 0.0);

		assertEquals(a, b);
	}

	@Test
	@DisplayName("Coord should not equal Vector even with same cm coordinates")
	void coordShouldNotEqualVectorEvenWithSameCoordinates() {
		ISU.Coord c = isu.new Coord(cell(1), cell(2));
		ISU.Vector v = isu.new Vector(cell(1), cell(2));

		assertNotEquals(c, v);
		assertNotEquals(v, c);
	}

	@Test
	@DisplayName("Geometry object should not equal null or unrelated object")
	void geometryShouldNotEqualNullOrOtherObject() {
		ISU.Coord c = isu.new Coord(cell(1), cell(2));

		assertNotEquals(null, c);
		assertNotEquals("hello", c);
	}

	@Test
	@DisplayName("Reference wrap formula should match ISU normalization in cm")
	void wrappedCmCoordinatesShouldMatchReferenceModuloFormula() {
		double x = -cell(3);
		double y = worldHeightCm + cell(4);

		ISU.Coord c = isu.new Coord(x, y);

		assertCoordEquals(
			wrapCm(x, worldWidthCm),
			wrapCm(y, worldHeightCm),
			c
		);
	}
}