/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.jaspersoft.ireport.designer.sheet.editors.box;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;

/**
 * @author Jerry Huxtable
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CompoundStroke implements Stroke {
   public final static int ADD = 0;
   public final static int DIFFERENCE = 3;
   public final static int INTERSECT = 2;
   public final static int SUBTRACT = 1;

   private int operation = ADD;
   private Stroke stroke1, stroke2;

   public CompoundStroke() {
   }

   public CompoundStroke( Stroke stroke1, Stroke stroke2, int operation ) {
      this.stroke1 = stroke1;
      this.stroke2 = stroke2;
      this.operation = operation;
   }

   public Shape createStrokedShape( Shape shape ) {
      if( stroke1 == null ){
         throw new IllegalArgumentException( "stroke1 is null" );
      }
      if( stroke2 == null ){
         throw new IllegalArgumentException( "stroke2 is null" );
      }

      Area area1 = new Area( stroke1.createStrokedShape( shape ) );
      Area area2 = new Area( stroke2.createStrokedShape( shape ) );
      switch( operation ){
         case ADD:
            area1.add( area2 );
            break;
         case SUBTRACT:
            area1.subtract( area2 );
            break;
         case INTERSECT:
            area1.intersect( area2 );
            break;
         case DIFFERENCE:
            area1.exclusiveOr( area2 );
            break;
      }
      return area1;
   }

   public int getOperation() {
      return operation;
   }

   public Stroke getStroke1() {
      return stroke1;
   }

   public Stroke getStroke2() {
      return stroke2;
   }

   public void setOperation( int operation ) {
      this.operation = operation;
   }

   public void setStroke1( Stroke stroke1 ) {
      this.stroke1 = stroke1;
   }

   public void setStroke2( Stroke stroke2 ) {
      this.stroke2 = stroke2;
   }
}