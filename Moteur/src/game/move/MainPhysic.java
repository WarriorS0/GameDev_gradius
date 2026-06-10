package game.move;

import java.util.ArrayList;
import java.util.List;

import engine.Grid;
import game.Game;
import game.entity.Ghost;
import game.entity.PacMan;
import engine.Entity;
import oop.tasks.Runtime;
import oop.tasks.Runnable;

public class MainPhysic implements Runnable{

	public static void main(String[] args) throws Exception {
		Runnable r = new MainPhysic();
		Runtime.boot(r);
	}

	@Override
	public void run() throws Exception {
		Game game = new Game(10,10);
		Grid grid = game.grid;
		Model model = new Model(grid);
		Ticker tick = new Ticker(model);
		PacMan p = new PacMan();
		p.setBounding();
		p.setPosition(game.grid.new Position(0, 0));
		Ghost red = new Ghost();
		red.setBounding();
		red.setPosition(game.grid.new Position(6,6));

		List<Entity> list = new ArrayList<>();
		list.add(p); list.add(red); //list.add(yellow); list.add(pink); list.add(blue);
		
		model.add(p);
		BasicStunt stunt_p = new BasicStunt(model, p);
		stunt_p.set(game.isu.new Vector(10,0));
		stunt_p.set_aSpeed(10);
		
		model.add(red);
		BasicStunt stunt_red = new BasicStunt(model, red);
		stunt_red.set(game.isu.new Vector(0,0));
		
//		model.add(yellow);
//		BasicStunt stunt_yellow = new BasicStunt(model, yellow);
//		stunt_yellow.set(game.isu.new Vector(15,15));
//		
//		model.add(pink);
//		BasicStunt stunt_pink = new BasicStunt(model, pink);
//		stunt_pink.set(game.isu.new Vector(15,15));
//		
//		model.add(blue);
//		BasicStunt stunt_blue = new BasicStunt(model, blue);
//		stunt_blue.set(game.isu.new Vector(0,0));
		
		tick.run();
		
	}

}


























