package engine.gal.aut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.gal.actions.GALAction;
import engine.gal.actions.iGALAction;
import engine.gal.actions.Move;
import engine.gal.actions.Turn;
import engine.gal.actions.Get;
import engine.gal.actions.Hit;
import engine.gal.actions.Protect;


import engine.gal.condition.GALCondition;
import engine.gal.condition.iGALCondition;
import engine.gal.condition.AtStep;
import engine.gal.condition.Conjunction;
import engine.gal.condition.KeyCondition;
import engine.gal.condition.Struck;
import engine.gal.condition.Life;
import engine.gal.condition.Timer;

import engine.gal.arguments.Category;
import engine.gal.arguments.Direction;

import gal.ast.AST;
import gal.ast.Parameter;
import gal.ast.IntValue;

public class AST2Aut {
	private List<Automaton> list;

	public AST2Aut(AST ast) {
		this.list = new ArrayList<>();

		for (gal.ast.Automaton aut : ast.aut_list) {
			Map<String, State> stateMap = new HashMap<>();
			int stateIdCounter = 0;

			State initialEngineState = new State(aut.initial_state.name, stateIdCounter++);
			stateMap.put(aut.initial_state.name, initialEngineState);

			for (gal.ast.Mode md : aut.modes) {
				if (!stateMap.containsKey(md.state.name)) {
					stateMap.put(md.state.name, new State(md.state.name, stateIdCounter++));
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

	// --- CONVERSION METHODS ---

	private iGALCondition convertCondition(gal.ast.Condition astCondition) {
		if (astCondition == null || astCondition.expression() == null) {
			return GALCondition.TRUE;
		}
		return convertExpression(astCondition.expression());
	}

	private iGALCondition convertExpression(gal.ast.Expression expr) {
		if (expr == null) {
			return GALCondition.TRUE;
		}

		// --- CASE 1: Standard Condition (FunCall) ---
		if (expr instanceof gal.ast.FunCall) {
			gal.ast.FunCall call = (gal.ast.FunCall) expr;
			String condName = call.name;

			if (condName == null) {
				return GALCondition.TRUE;
			}

			switch (condName.toLowerCase()) {
			case "true":
				return GALCondition.TRUE;

			case "atstep":
				if (call.parameters.size() >= 3) {
					try {
						String dirParam = call.parameters.get(0).toString();
						String catParam = call.parameters.get(1).toString();

						Direction dir = Direction.canonical(dirParam);
						Category cat = Category.canonical(catParam);

						// Check parameter type for the step value
						int step = 1;
						Parameter p2 = call.parameters.get(2);
						if (p2 instanceof IntValue) {
							step = ((IntValue) p2).value;
						} else {
							step = Integer.parseInt(p2.toString());
						}

						return new AtStep(dir, cat, step);

					} catch (Exception e) {
						throw e;
					}
				} else {
					// AtStep requires 3 parameters (Direction, Category, nbStep) so we just do
					// nothing
				}
				return GALCondition.TRUE;

			case "key":
				if (call.parameters.size() >= 1) {
					String keyName = call.parameters.get(0).toString();
					return new KeyCondition(keyName);
				}
				throw new IllegalArgumentException("Key condition requires 1 parameter");
				
			case "struck":
				if (call.parameters.size() >= 1) {
					Category category = Category.canonical(call.parameters.get(0).toString());
					return new Struck(category);
				}
				return new Struck();
			case "life":
				if (call.parameters.size() >= 1) {
					Parameter p = call.parameters.get(0);

					if (p instanceof IntValue) {
						return new Life(((IntValue) p).value);
					}

					return new Life(Integer.parseInt(p.toString()));
				}

				throw new IllegalArgumentException("Life condition requires 1 parameter");
				
			case "timer":
				return new Timer();
			default:
				return GALCondition.FALSE;
			}
		}

		// --- CASE 2: Conjunction (BinaryOp) ---
		else if (expr instanceof gal.ast.BinaryOp) {
			gal.ast.BinaryOp binOp = (gal.ast.BinaryOp) expr;
			if ("&".equals(binOp.operator) || "and".equalsIgnoreCase(binOp.operator)) {
				Conjunction conjunction = new Conjunction();
				conjunction.add(convertExpression(binOp.left_operand));
				conjunction.add(convertExpression(binOp.right_operand));
				return conjunction;
			}
		}

		// --- CASE 3: Negation (UnaryOp) ---
		else if (expr instanceof gal.ast.UnaryOp) {
			gal.ast.UnaryOp unOp = (gal.ast.UnaryOp) expr;
			if ("!".equals(unOp.operator) || "not".equalsIgnoreCase(unOp.operator)) {
				System.err.println("Fact: The engine class for Negation/Not is missing. Returning TRUE fallback.");
				// iGALCondition subCondition = convertExpression(unOp.operand);
				// need to implement Not
				// return new engine.gal.condition.Not(subCondition);
			}
		}

		return GALCondition.FALSE;
	}

	private iGALAction convertAction(gal.ast.Actions astAction) {
		if (astAction == null || astAction.actions.isEmpty()) {
			return GALAction.NOTHING;
		}

		gal.ast.FunCall call = astAction.actions.getFirst();
		String actionName = call.name;

		if (actionName == null) {
			return GALAction.NOTHING;
		}

		switch (actionName.toLowerCase()) {
		case "move":
			if (call.parameters.size() >= 1) {
				try {
					String dirStr = call.parameters.get(0).toString();
					Direction dir = Direction.canonical(dirStr);

					if (call.parameters.size() >= 2) {
						Parameter p2 = call.parameters.get(1);
						if (p2 instanceof IntValue) {
							return new Move(dir, ((IntValue) p2).value);
						}
					}
					return new Move(dir, 1);
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
			return new Move();

		case "turn":
			if (call.parameters.size() >= 1) {
				Parameter p = call.parameters.get(0);
				if (p instanceof IntValue) {
					return new Turn(((IntValue) p).value);
				} else {
					try {
						Direction dir = Direction.canonical(p.toString());
						return new Turn(dir);
					} catch (IllegalArgumentException e) {
						throw e;
					}
				}
			}
			return new Turn(0);

		case "hit":
			if (call.parameters.size() >= 1) {
				Parameter p = call.parameters.get(0);

				if (p instanceof IntValue) {
					return new Hit(((IntValue) p).value);
				}

				return new Hit(Integer.parseInt(p.toString()));
			}

			return new Hit(1);
		case "rest":
			return GALAction.NOTHING;
		case "get":
			if (call.parameters.size() >= 1) {
				Category category = Category.canonical(call.parameters.get(0).toString());
				return new Get(category);
			}
			return new Get(null);
		case "protect":
			if (call.parameters.size() >= 1) {
				Parameter p = call.parameters.get(0);

				if (p instanceof IntValue) {
					return new Protect(((IntValue) p).value);
				}

				return new Protect(Double.parseDouble(p.toString()));
			}

			throw new IllegalArgumentException("Protect action requires 1 parameter");

		default:
			return GALAction.NOTHING;
		}
	}
}