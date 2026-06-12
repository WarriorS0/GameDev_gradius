package game.move;

import engine.entity.Entity;
import engine.geometry.Grid;
import game.Game;
import game.entity.Ghost;
import game.entity.PacMan;
import oop.tasks.Runtime;
import oop.tasks.Runnable;

public class MainPhysic implements Runnable {

	public static void main(String[] args) throws Exception {
		Runnable r = new MainPhysic();
		Runtime.boot(r);
	}

	@Override
	public void run() throws Exception {
		Game game = new Game(10, 10);
		Grid grid = game.grid;

		engine.move.Model model = new engine.move.Model(grid);

		PacMan pacman = new PacMan();
		place(pacman, 0, 0);

		Ghost red = new Ghost();
		place(red, 6, 6);

		model.add(pacman);
		BasicStunt pacmanStunt = new BasicStunt(model, pacman);
		pacmanStunt.set(game.isu.new Vector(10, 0));
		pacmanStunt.set_aSpeed(10);

		model.add(red);
		BasicStunt redStunt = new BasicStunt(model, red);
		redStunt.set(game.isu.new Vector(0, 0));
		redStunt.set_aSpeed(0);

		new engine.move.Ticker(model);
	}

	private void place(Entity entity, int x, int y) {
		Game game = Game.game();
		entity.place(game.grid.new Position(x, y));
	}
}