package game.gradius.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import engine.entity.Entity;
import engine.gal.GALBot;
import engine.gal.aut.AST2Aut;
import engine.gal.aut.Automaton;
import engine.geometry.Grid;
import engine.geometry.ISU;
import engine.graphics.avatars.Avatar;
import engine.move.Model;
import gal.ast.AST;
import gal.parser.Parser;
import game.Game;
import game.gradius.graphics.DragonBodyAvatar;
import game.gradius.graphics.DragonHeadAvatar;
import game.gradius.stunt.FollowerStunt;
import game.gradius.stunt.FollowerStunt.MovementState;
import game.gradius.stunt.LeaderStunt;

public class Dragon extends Entity {
	
	private List<Entity> dragon_parts;
	private final static int TICK_DELAY = 10;
	private final static Model model = Game.game().model;
	
	private Automaton loadAutomaton(String galFilePath, String automatonName) {
		try {
			AST ast = Parser.from_file(galFilePath);
			AST2Aut converter = new AST2Aut(ast);

			for (Automaton automaton : converter.getAutomata()) {
				if (automaton.name().equals(automatonName)) {
					return automaton;
				}
			}

			throw new IllegalArgumentException("Automaton not found: " + automatonName);

		} catch (Exception e) {
			throw new RuntimeException("Cannot load GAL automaton from: " + galFilePath, e);
		}
	}

	public Dragon(int nb_segments) {
		super("Dragon");
		dragon_parts = new ArrayList<Entity>();
		DragonHead head = new DragonHead();
		model.add(head);
		GALBot dragonBot = new GALBot(head);
		dragonBot.healthPercent(3);
		head.bot(dragonBot);
		LeaderStunt headStunt = new LeaderStunt(model, head);
		model.add(head, headStunt);
		headStunt.setMaxLinearSpeed(40);
		headStunt.setMaxAngularSpeed(45);
		
		Automaton dragonAutomaton = loadAutomaton("src/game/gradius/gal/passiveDragon.gal", "PassiveDragon");
		dragonBot.set(dragonAutomaton);
		
		DragonHeadAvatar headAvatar = new DragonHeadAvatar(head);
		headAvatar.set_z_order(Avatar.MAX_ZORDER-1);
		dragon_parts.add(head);
		LinkedList<MovementState> leaderHistory = headStunt.getHistory();
		Entity previousPart = head;
		for(int i=0; i<nb_segments; i++) {
			DragonBody body = new DragonBody();
			FollowerStunt bodyStunt = new FollowerStunt(model, body, previousPart, leaderHistory, TICK_DELAY);
			model.add(body, bodyStunt);
			DragonBodyAvatar bodyAvatar = new DragonBodyAvatar(body);
			bodyAvatar.set_z_order(Avatar.MAX_ZORDER-i-2);
			dragon_parts.add(body);
			
			previousPart = body;
			leaderHistory = bodyStunt.getMyHistory();
		}
		//headStunt.setAngularSpeed(45);
		//headStunt.setLinearSpeed(isu.new Vector(0, 20));
		place(isu.new Coord(0,0));
	}
	
	public Entity getHead() {
		return dragon_parts.get(0);
	}
	
	@Override
	public void place(ISU.Coord center) {
		super.place(center); 
		
		for(Entity part: dragon_parts) {
			part.place(center);
		}
	}
	
	@Override
	public void place(Grid.Position pos) {
		super.place(pos); 
		
		for(Entity part: dragon_parts) {
			part.place(pos);
		}
	}
	
	@Override
	public void forceOrientation(double orientation) {
		dragon_parts.get(0).forceOrientation(orientation);
	}
	
	@Override
	public void kill() {
	    dragon_parts.get(0).kill();
	}

	@Override
	protected void setBounding() {
		clearBounding();
	}

}