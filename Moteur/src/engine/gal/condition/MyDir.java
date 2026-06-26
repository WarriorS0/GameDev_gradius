package engine.gal.condition;

import engine.entity.Entity;
import engine.gal.arguments.Direction;

public class MyDir implements iGALCondition {

    private static final double TOLERANCE = 11.25;

    private final Direction direction;

    public MyDir(Direction direction) {
        if (direction == null)
            throw new IllegalArgumentException("direction cannot be null");
        this.direction = direction;
    }

    @Override
    public boolean eval(Entity e) {
        if (e == null) return false;

        double entityAngle = e.orientation();
        double targetAngle = direction.toAngle();

        double diff = Math.abs(entityAngle - targetAngle) % 360.0;
        if (diff > 180.0) diff = 360.0 - diff;

        return diff <= TOLERANCE;
    }
}