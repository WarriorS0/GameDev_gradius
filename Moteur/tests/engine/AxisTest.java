package engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AxisTest {

    private static final double EPSILON = 1e-9;

    @Test
    void constructorShouldRejectNegativePerimeter() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Axis(true, -5);
        });
    }

    @Test
    void constructorShouldRejectZeroPerimeter() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Axis(true, 0);
        });
    }

    @Test
    void normalizeIntShouldReturnSameLengthWhenNotOnTorus() {
        Axis axis = new Axis(false, 5);
        assertEquals(7, axis.normalize(7));
        assertEquals(-2, axis.normalize(-2));
    }

    @Test
    void normalizeIntShouldWrapPositiveLengthOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(2, axis.normalize(7));
        assertEquals(0, axis.normalize(5));
        assertEquals(4, axis.normalize(4));
    }

    @Test
    void normalizeIntShouldWrapNegativeLengthOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(4, axis.normalize(-1));
        assertEquals(3, axis.normalize(-2));
        assertEquals(0, axis.normalize(-5));
    }

    @Test
    void normalizeDoubleShouldReturnSameLengthWhenNotOnTorus() {
        Axis axis = new Axis(false, 5.0);

        assertEquals(7.2, axis.normalize(7.2), EPSILON);
        assertEquals(-3.4, axis.normalize(-3.4), EPSILON);
    }

    @Test
    void normalizeDoubleShouldWrapInCenteredIntervalOnTorus() {
        Axis axis = new Axis(true, 5.0);

        assertEquals(2.0, axis.normalize(7.0), EPSILON);
        assertEquals(4.0, axis.normalize(4.0), EPSILON);
        assertEquals(2.0, axis.normalize(-3.0), EPSILON);
    }

    @Test
    void distanceShouldBeAbsoluteDifferenceWhenNotOnTorus() {
        Axis axis = new Axis(false, 10.0);

        assertEquals(8.0, axis.distance(1.0, 9.0), EPSILON);
        assertEquals(3.0, axis.distance(2.0, 5.0), EPSILON);
    }

    @Test
    void distanceShouldTakeShortestPathWhenOnTorus() {
        Axis axis = new Axis(true, 10.0);

        assertEquals(2.0, axis.distance(1.0, 9.0), EPSILON);
        assertEquals(3.0, axis.distance(2.0, 5.0), EPSILON);
        assertEquals(0.0, axis.distance(3.0, 3.0), EPSILON);
    }
}