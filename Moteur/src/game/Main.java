package game;

import game.entity.PacMan;

import java.util.logging.Logger;

import engine.logs.LoggerManager;
import game.entity.Gum;

public class Main {

	private static Logger logger = LoggerManager.getLogger(Main.class.getName());

	public static void main(String args[]) {
		Game game = new Game(12, 12);

		PacMan p = new PacMan();
		Gum g = new Gum();

		g.place(game.grid.new Position(10, 10));

		logger.info(String.valueOf(p.intersects(g)));
	}
}
