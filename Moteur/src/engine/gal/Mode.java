package engine.gal;

import java.util.HashMap;
import java.util.Map;

public class Mode {

	// STATIC

	private static final Map<String, Mode> modes = new HashMap<>();

	// CONSTANTS

	public static final Mode Blocking = canonical("Blocking");
	public static final Mode Dying = canonical("Dying");
	public static final Mode Escaping = canonical("Escaping");
	public static final Mode Fighting = canonical("Fighting");
	public static final Mode Resting = canonical("Resting");
	public static final Mode Running = canonical("Running");
	public static final Mode Searching = canonical("Searching");
	public static final Mode Sleeping = canonical("Sleeping");
	public static final Mode Waiting = canonical("Waiting");
	public static final Mode Walking = canonical("Walking");

	// FIELD

	private String name;

	// CONSTRUCTOR

	private Mode(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Mode name cannot be null");
		}

		this.name = name;
	}

	// FACTORY

	public static Mode canonical(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Mode name cannot be null");
		}

		Mode mode = modes.get(name);

		if (mode == null) {
			mode = new Mode(name);
			modes.put(name, mode);
		}

		return mode;
	}

	// GETTER

	public String name() {
		return this.name;
	}

	// EQUALS

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Mode)) {
			return false;
		}

		Mode mode = (Mode) o;
		return this.name.equals(mode.name);
	}

	public boolean equals(Mode mode) {
		return this.equals((Object) mode);
	}

	// HASH

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	// TO STRING

	@Override
	public String toString() {
		return this.name;
	}
}