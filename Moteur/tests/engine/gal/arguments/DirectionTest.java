package engine.gal.arguments;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DirectionTest {

	@Test
	void canonicalReturnsSameDirectionForAliases() {
		assertSame(Direction.N, Direction.canonical("N"));
		assertSame(Direction.N, Direction.canonical("North"));
		assertSame(Direction.F, Direction.canonical("F"));
		assertSame(Direction.F, Direction.canonical("Forward"));
		assertSame(Direction.F, Direction.canonical("Front"));
	}

	@Test
	void absoluteAndRelativeDirectionsAreCorrect() {
		assertTrue(Direction.N.isAbsolute());
		assertTrue(Direction.S.isAbsolute());
		assertTrue(Direction.E.isAbsolute());
		assertTrue(Direction.W.isAbsolute());

		assertTrue(Direction.F.isRelative());
		assertTrue(Direction.B.isRelative());

		assertTrue(Direction.H.isHere());
	}

	@Test
	void absoluteDirectionsHaveAngles() {
		assertEquals(0, Direction.E.toAngle());
		assertEquals(90, Direction.S.toAngle());
		assertEquals(180, Direction.W.toAngle());
		assertEquals(270, Direction.N.toAngle());
	}

	@Test
	void relativeDirectionHasNoAbsoluteAngle() {
		assertThrows(IllegalStateException.class, () -> Direction.F.toAngle());
	}
}