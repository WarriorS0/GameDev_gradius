package engine;

import java.io.PrintStream;

import game.Game;

public class Grid {

  // FIELDS

   ISU isu;
   Axis xAxis, yAxis;

   int width_ncell, height_ncell;

   Cell[][] grid;

  // CONSTRUCTOR

   public Grid(Game game) { throw new UnsupportedOperationException("Unimplemented method"); }

  // INIT

  void init() { throw new UnsupportedOperationException("Unimplemented method"); }

  // GETTER

   int width() { return 0; }

   int height() { return 0; }

   Grid.Cell cellAt(Grid.Position p) { return null; }

  // SHOW

   void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }



  // == DIMENSION (nb cell) ==

   class Dimension {
     int x_ncell, y_ncell;

    // CONSTRUCTOR

     Dimension(int x_ncell, int y_ncell) { throw new UnsupportedOperationException("Unimplemented method"); }

    // GETTER

     int x() { return 0; }

     int y() { return 0; }

    // GEOMETRY

     void normalize() { throw new UnsupportedOperationException("Unimplemented method"); }

    // EQUALS / EQUIV
     boolean equals(Object o) { return false; }

     boolean equiv(Dimension d) { return false; }

    // CONVERSION

     ISU.Dimension toISUDimension() { return null; }

    // SHOW

    void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

  }

  // == VECTOR ==

   class Vector  {

    // CONSTRUCTOR

     Vector(int x_ncell, int y_ncell) { throw new UnsupportedOperationException("Unimplemented method"); }

    // OPERATION

     void add(Vector v) { throw new UnsupportedOperationException("Unimplemented method"); }

    // SHOW

    void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

  }

  // == POINT ==

   class Position  {

    // CONSTRUCTOR

     Position(int x_ncell, int y_ncell) { throw new UnsupportedOperationException("Unimplemented method"); }

    // COPY ? if needed

     Grid.Position copy() { return null; }

    // EQUALS
     boolean equals(Object o) { return false; }

    // TRANSLATION

     void translate(Vector v) { throw new UnsupportedOperationException("Unimplemented method"); }

     void moveNorth(int n_ncell) { throw new UnsupportedOperationException("Unimplemented method"); }

    // ROTATION ? if needed

     void rotateAround(Grid.Position position, int angle_degree) { throw new UnsupportedOperationException("Unimplemented method"); }

    // DISTANCE

     double distanceTo(Position p) { return 0.0;   }

    // CONVERSION

     ISU.Coord toISUCoord() { return null; }

     ISU.Coord toISUCoordCentered() { return null; }

     Picture.Pixel toPicturePixel() { return null; }

    // SHOW

    void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

  }

  // === CELL ===

   class Cell {

     Grid.Dimension size;
    Grid.Position position;
     List<Entity> entities;

    // CONSTRUCTOR

    Cell(Position p) { throw new UnsupportedOperationException("Unimplemented method"); }

    // ADD

     void add(Entity e) { throw new UnsupportedOperationException("Unimplemented method"); }

    // REMOVE

     void remove(Entity e) { throw new UnsupportedOperationException("Unimplemented method"); }

    // PREDICATE

     boolean contains(Entity e) { return false; }

    // SHOW

    void show(PrintStream ps) { throw new UnsupportedOperationException("Unimplemented method"); }

  }
}
