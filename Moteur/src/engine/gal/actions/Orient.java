package engine.gal.actions;

import engine.entity.Entity;
import engine.gal.arguments.Direction;

public class Orient extends GALAction {

    private final double angle_deg;

    public Orient(Direction direction) {
        super(1.0);
        this.angle_deg = direction.toAngle();
    }

    @Override
    public boolean exec(Entity e) {
        if (e == null) return false;
        e.forceOrientation(angle_deg);
        return true;
    }
}