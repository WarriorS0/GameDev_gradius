package game.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.Game;

class EntityCollisionTest {

	@BeforeEach
	void initGame() {
		new Game(30, 30);
	}

	// =========================
	// Helpers de test
	// =========================

	private static class TestPacMan extends PacMan {
		void putAt(int x, int y) {
			super.setPosition(super.grid.new Position(x, y));
			super.setBounding();
		}
	}

	private static class TestGum extends Gum {
		void putAt(int x, int y) {
			super.setPosition(super.grid.new Position(x, y));
			super.setBounding();
		}
	}

	private static class TestBoss extends Boss {
		void putAt(int x, int y) {
			super.setPosition(super.grid.new Position(x, y));
			super.setBounding();
		}

		void rotate(int angleDegree) {
			super.turn(angleDegree);
			super.setBounding();
		}
	}

	// =========================
	// PacMan / Gum
	// =========================

	@Test
	void pacmanAndGumSameCellIntersect() {
		TestPacMan pacman = new TestPacMan();
		TestGum gum = new TestGum();

		pacman.putAt(10, 10);
		gum.putAt(10, 10);

		assertTrue(pacman.intersects(gum));
		assertTrue(gum.intersects(pacman));
	}

	@Test
	void pacmanAndGumFarAwayDoNotIntersect() {
		TestPacMan pacman = new TestPacMan();
		TestGum gum = new TestGum();

		pacman.putAt(10, 10);
		gum.putAt(5, 5);

		assertFalse(pacman.intersects(gum));
		assertFalse(gum.intersects(pacman));
	}

	// =========================
	// PacMan / Obstacle
	// =========================

	@Test
	void pacmanAndObstacleSameCellIntersect() {
		TestPacMan pacman = new TestPacMan();
		pacman.putAt(10, 10);

		Obstacle obstacle = new Obstacle(10, 10);

		assertTrue(pacman.intersects(obstacle));
		assertTrue(obstacle.intersects(pacman));
	}

	// =========================
	// Boss / Obstacle
	// =========================

	@Test
	void bossAndObstacleOnHorizontalArmIntersect() {
		TestBoss boss = new TestBoss();
		boss.putAt(20, 20);

		// Le Boss non tourné a une branche horizontale vers la droite.
		Obstacle obstacle = new Obstacle(21, 20);

		assertTrue(boss.intersects(obstacle));
		assertTrue(obstacle.intersects(boss));
	}

	@Test
	void bossTurnedNinetyDegreesAndObstacleOnNewArmIntersect() {
		TestBoss boss = new TestBoss();
		boss.putAt(20, 20);

		boss.rotate(90);

		// Après rotation de 90°, la branche qui partait à droite part vers le bas.
		Obstacle obstacle = new Obstacle(20, 21);

		assertTrue(boss.intersects(obstacle));
		assertTrue(obstacle.intersects(boss));
	}
}