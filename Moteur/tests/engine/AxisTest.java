package engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AxisTest {

    private static final double EPSILON = 1e-9;

    // =========================
    // CONSTRUCTOR
    // =========================

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
    void constructorShouldAcceptPositivePerimeter() {
        assertDoesNotThrow(() -> {
            new Axis(true, 5);
        });
    }

    // =========================
    // NORMALIZE INT
    // =========================

    @Test
    void normalizeIntShouldReturnSameLengthWhenNotOnTorus() {
        Axis axis = new Axis(false, 5);

        assertEquals(7, axis.normalize(7));
        assertEquals(-2, axis.normalize(-2));
        assertEquals(0, axis.normalize(0));
    }

    @Test
    void normalizeIntShouldWrapPositiveLengthOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(0, axis.normalize(0));
        assertEquals(1, axis.normalize(1));
        assertEquals(4, axis.normalize(4));
        assertEquals(0, axis.normalize(5));
        assertEquals(1, axis.normalize(6));
        assertEquals(2, axis.normalize(7));
    }

    @Test
    void normalizeIntShouldWrapNegativeLengthOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(4, axis.normalize(-1));
        assertEquals(3, axis.normalize(-2));
        assertEquals(0, axis.normalize(-5));
        assertEquals(4, axis.normalize(-6));
    }

    // =========================
    // NORMALIZE DOUBLE
    // =========================

    @Test
    void normalizeDoubleShouldReturnSameLengthWhenNotOnTorus() {
        Axis axis = new Axis(false, 5);

        assertEquals(7.5, axis.normalize(7.5), EPSILON);
        assertEquals(-2.5, axis.normalize(-2.5), EPSILON);
        assertEquals(0.0, axis.normalize(0.0), EPSILON);
    }

    @Test
    void normalizeDoubleShouldWrapPositiveLengthOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(0.0, axis.normalize(0.0), EPSILON);
        assertEquals(1.5, axis.normalize(1.5), EPSILON);
        assertEquals(0.0, axis.normalize(5.0), EPSILON);
        assertEquals(1.5, axis.normalize(6.5), EPSILON);
        assertEquals(2.5, axis.normalize(7.5), EPSILON);
    }

    @Test
    void normalizeDoubleShouldWrapNegativeLengthOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(4.0, axis.normalize(-1.0), EPSILON);
        assertEquals(2.5, axis.normalize(-2.5), EPSILON);
        assertEquals(0.0, axis.normalize(-5.0), EPSILON);
        assertEquals(4.5, axis.normalize(-5.5), EPSILON);
    }

    // =========================
    // DISTANCE
    // =========================

    @Test
    void distanceShouldBeAbsoluteDifferenceWhenNotOnTorus() {
        Axis axis = new Axis(false, 5);

        assertEquals(3.0, axis.distance(1.0, 4.0), EPSILON);
        assertEquals(8.0, axis.distance(-2.0, 6.0), EPSILON);
    }

    @Test
    void distanceShouldUseShortestPathWhenOnTorus() {
        Axis axis = new Axis(true, 5);

        assertEquals(1.0, axis.distance(0.0, 1.0), EPSILON);
        assertEquals(2.0, axis.distance(0.0, 2.0), EPSILON);
        assertEquals(1.0, axis.distance(0.0, 4.0), EPSILON);
        assertEquals(2.0, axis.distance(1.0, 4.0), EPSILON);
    }

    @Test
    void distanceShouldBeSymmetric() {
        Axis axis = new Axis(true, 5);

        assertEquals(axis.distance(1.0, 4.0), axis.distance(4.0, 1.0), EPSILON);
        assertEquals(axis.distance(0.0, 4.0), axis.distance(4.0, 0.0), EPSILON);
    }
}