package engine.gal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import engine.gal.actions.Move;
import engine.gal.actions.Nothing;
import engine.gal.actions.Turn;
import engine.gal.arguments.Direction;
import engine.gal.aut.Automaton;
import engine.gal.aut.State;
import engine.gal.aut.Transition;
import engine.move.Model;
import game.Game;
import game.entity.PacMan;

class GALIntegrationTest {

	private static final double EPSILON = 1e-9;

	private Game game;
	private Model model;
	private PacMan pacman;
	private GALBot bot;
	private GALStunt stunt;

	@BeforeEach
	void setup() {
		game = new Game(30, 30);
		model = new Model(game.grid);

		pacman = new PacMan();
		model.add(pacman);

		bot = new GALBot(pacman);
		pacman.bot(bot);

		stunt = new GALStunt(model, pacman);
		stunt.setMaxLinearSpeed(10.0);
		stunt.setMaxAngularSpeed(90.0);
	}

	@Test
	void moveExecStartsLinearMovement() {
		Move move = new Move(Direction.E, 1.0, 1);

		assertTrue(move.exec(pacman));

		assertEquals(10.0, model.linearSpeed(pacman).x(), EPSILON);
		assertEquals(0.0, model.linearSpeed(pacman).y(), EPSILON);
		assertTrue(stunt.actionDuration() > 0.0);
	}

	@Test
	void stuntTickFinishesMoveAndStopsEntity() {
		assertTrue(stunt.startMoving(Direction.E, 1.0, 100.0));

		assertEquals(10.0, model.linearSpeed(pacman).x(), EPSILON);

		stunt.tick(100.0);

		assertEquals(0.0, model.linearSpeed(pacman).x(), EPSILON);
		assertEquals(0.0, model.linearSpeed(pacman).y(), EPSILON);
		assertEquals(0.0, stunt.actionDuration(), EPSILON);
	}

	@Test
	void turnExecStartsAngularMovement() {
		Turn turn = new Turn(90, 1.0);

		assertTrue(turn.exec(pacman));

		assertEquals(90.0, model.angularSpeed(pacman), EPSILON);
		assertTrue(stunt.actionDuration() > 0.0);
	}

	@Test
	void stuntTickFinishesTurnAndSetsFinalOrientation() {
		assertTrue(stunt.startTurning(90, 1.0));

		stunt.tick(1000.0);

		assertEquals(90.0, pacman.orientation(), EPSILON);
		assertEquals(0.0, model.angularSpeed(pacman), EPSILON);
		assertEquals(0.0, stunt.actionDuration(), EPSILON);
	}

	@Test
	void transitionChangesBotStateWhenConditionAndActionAreTrue() {
		State source = new State("Walking", 0);
		State target = new State("Walking", 1);

		bot.state(source);

		Transition transition = new Transition(source, e -> true, e -> true, target);

		assertTrue(transition.exec(pacman));
		assertEquals(target, bot.state());
	}

	@Test
	void transitionDoesNothingWhenSourceStateDoesNotMatch() {
		State source = new State("Walking", 0);
		State other = new State("Walking", 1);
		State target = new State("Walking", 2);

		bot.state(other);

		Transition transition = new Transition(source, e -> true, e -> true, target);

		assertFalse(transition.exec(pacman));
		assertEquals(other, bot.state());
	}

	@Test
	void automatonInitializesBotStateAndExecutesTransition() {
		State initial = new State("Walking", 0);
		State target = new State("Walking", 1);

		Automaton automaton = new Automaton("test", initial);

		automaton.add(new Transition(initial, e -> true, new Nothing(), target));

		bot.set(automaton);

		assertNull(bot.state());

		assertTrue(automaton.step(pacman));
		assertEquals(target, bot.state());
	}

	@Test
	void automatonTakesFirstValidTransitionInOrder() {
		State initial = new State("Walking", 0);
		State firstTarget = new State("Walking", 1);
		State secondTarget = new State("Walking", 2);

		Automaton automaton = new Automaton("test", initial);

		automaton.add(new Transition(initial, e -> true, new Nothing(), firstTarget));

		automaton.add(new Transition(initial, e -> true, new Nothing(), secondTarget));

		bot.set(automaton);

		assertTrue(automaton.step(pacman));
		assertEquals(firstTarget, bot.state());
	}

	@Test
	void modelTickWakesGalStuntAndBot() {
		State initial = new State("Walking", 0);
		State target = new State("Walking", 1);

		Automaton automaton = new Automaton("walk", initial);

		automaton.add(new Transition(initial, e -> true, new Move(Direction.E, 1.0, 1), target));

		bot.set(automaton);

		assertNull(bot.state());

		model.tick(0.1);

		assertEquals(target, bot.state());
		assertTrue(model.linearSpeed(pacman).x() > 0.0);
	}
}