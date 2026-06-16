package engine.gal.arguments;

import java.util.HashMap;
import java.util.Map;

public class Category {

	// CONSTANTS

	public static final Category Adversary;
	public static final Category Obstacle;
	public static final Category Team;
	public static final Category Void;

	// STATIC

	private static final Map<String, Category> categories = new HashMap<>();

	private static final int CATEGORY_COUNT = 4;
	private static final boolean[][] interaction = new boolean[CATEGORY_COUNT][CATEGORY_COUNT];

	// STATIC INITIALIZATION

	static {
		Void = new Category("Void", 0);
		Adversary = new Category("Adversary", 1);
		Obstacle = new Category("Obstacle", 2);
		Team = new Category("Team", 3);

		register(Void, "Void");
		register(Adversary, "Adversary");
		register(Obstacle, "Obstacle");
		register(Team, "Team");
	}

	private static void register(Category category, String name) {
		categories.put(name, category);
	}

	// FACTORY

	public static Category canonical(String name) {
		Category category = categories.get(name);

		if (category == null) {
			throw new IllegalArgumentException("Unknown category: " + name);
		}

		return category;
	}

	// CONSTRUCTOR

	private final String name;
	private final int index;

	private Category(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// SETTER

	/**
	 * @apiNote updates the interaction table such that <i>c1 interactsWith c2
	 *          &equiv; bool</i>
	 * @param c1
	 * @param c2
	 * @param bool
	 */
	public static void setInteraction(Category c1, Category c2, boolean bool) {
		interaction[c1.index][c2.index] = bool;
	}
	
	
	// GETTER
	
	public String name() {
		return name;
	}

	// PREDICATE

	/**
	 * @apiNote Check the table to see if there is an interaction between
	 *          {@code this} and the {@code c} category.
	 * @param c
	 */
	public boolean interactsWith(Category c) {
		return interaction[this.index][c.index];
	}

	@Override
	public String toString() {
		return name;
	}
}