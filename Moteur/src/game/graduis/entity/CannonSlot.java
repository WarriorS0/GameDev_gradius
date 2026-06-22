package game.graduis.entity;

public enum CannonSlot {
	TOP, BOTTOM;

	public boolean isTop() {
		return this == TOP;
	}

	public boolean isBottom() {
		return this == BOTTOM;
	}
}