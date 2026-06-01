package engine.entity;

import java.util.Set;

import engine.shape.iShape;

 class Bounding {

  // FIELDS

  Set<iShape> boundings;

  // CONSTRUCTOR

   Bounding(){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `Bounding`"); }

  // BUILDER

   void add(iShape shape){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `add`"); }

  // INTERSECTION

   boolean intersects(iShape shape){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`"); }

   boolean intersects(Bounding bounding){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`"); }

}
