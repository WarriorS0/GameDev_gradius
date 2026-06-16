package engine.gal.aut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.gal.actions.GALAction;
import engine.gal.actions.iGALAction;
import engine.gal.condition.GALCondition;
import engine.gal.condition.iGALCondition;
import gal.ast.AST;

public class AST2Aut {
	private List<Automaton> list;

	public AST2Aut(AST ast) {
		this.list = new ArrayList<>();
		for (gal.ast.Automaton aut : ast.aut_list) {
			Map<String, State> stateMap = new HashMap<>();
			State initialEngineState = new State(aut.initial_state.name, aut.initial_state.id);
			stateMap.put(aut.initial_state.name, initialEngineState);
			for (gal.ast.Mode md : aut.modes) {
				if (!stateMap.containsKey(md.state.name)) {
					stateMap.put(md.state.name, new State(md.state.name, md.state.id));
				}
			}
			Automaton engineAuto = new Automaton(aut.name, initialEngineState);
			for (gal.ast.Mode md : aut.modes) {
				State sourceState = stateMap.get(md.state.name);
				for (gal.ast.Transition tr : md.behaviour.transitions) {
					State targetState = stateMap.get(tr.target.name);
					iGALCondition condition = convertCondition(tr.condition);
					iGALAction action = convertAction(tr.action);
					Transition engineTransition = new Transition(sourceState, condition, action, targetState);
					engineAuto.add(engineTransition);
				}
			}

			this.list.add(engineAuto);
		}
	}

	public List<Automaton> getAutomata() {
		return list;
	}

	// --- MÉTHODES DE CONVERSION DÉDIÉES ---

	private iGALCondition convertCondition(gal.ast.Condition astCondition) {
		// NOTE: Le détail de gal.ast.Expression n'étant pas fourni,
		// je retourne la constante statique TRUE existante dans
		// engine.gal.condition.GALCondition.
		// Vous devrez ajouter ici votre logique de type "instanceof" ou un Visiteur.
		return GALCondition.TRUE;
	}

	private iGALAction convertAction(gal.ast.Actions astAction) {
		// NOTE: La classe gal.ast.Actions n'étant pas fournie,
		// je retourne l'instance existante engine.gal.actions.Nothing.
		// Vous devrez ajouter ici le mapping vers Move, etc.
		return GALAction.NOTHING;
	}
}