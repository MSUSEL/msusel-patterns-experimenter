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

import java.awt.*;
import java.awt.geom.*;

public class ShapeStroke implements Stroke {
	private Shape shapes[];
	private float advance;
	private boolean stretchToFit = false;
	private boolean repeat = true;
	private AffineTransform t = new AffineTransform();
	private static final float FLATNESS = 1;

	public ShapeStroke( Shape shapes, float advance ) {
		this( new Shape[] { shapes }, advance );
	}

	public ShapeStroke( Shape shapes[], float advance ) {
		this.advance = advance;
		this.shapes = new Shape[shapes.length];

		for ( int i = 0; i < this.shapes.length; i++ ) {
			Rectangle2D bounds = shapes[i].getBounds2D();
			t.setToTranslation( -bounds.getCenterX(), -bounds.getCenterY() );
			this.shapes[i] = t.createTransformedShape( shapes[i] );
		}
	}

	public Shape createStrokedShape( Shape shape ) {
		GeneralPath result = new GeneralPath();
		PathIterator it = new FlatteningPathIterator( shape.getPathIterator( null ), FLATNESS );
		float points[] = new float[6];
		float moveX = 0, moveY = 0;
		float lastX = 0, lastY = 0;
		float thisX = 0, thisY = 0;
		int type = 0;
		boolean first = false;
		float next = 0;
		int currentShape = 0;
		int length = shapes.length;

		float factor = 1;

		while ( currentShape < length && !it.isDone() ) {
			type = it.currentSegment( points );
			switch( type ){
			case PathIterator.SEG_MOVETO:
				moveX = lastX = points[0];
				moveY = lastY = points[1];
				result.moveTo( moveX, moveY );
				first = true;
				next = 0;
				break;

			case PathIterator.SEG_CLOSE:
				points[0] = moveX;
				points[1] = moveY;
				// Fall into....
			case PathIterator.SEG_LINETO:
				thisX = points[0];
				thisY = points[1];
				float dx = thisX-lastX;
				float dy = thisY-lastY;
				float distance = (float)Math.sqrt( dx*dx + dy*dy );
				if ( distance >= next ) {
					float r = 1.0f/distance;
					float angle = (float)Math.atan2( dy, dx );
					while ( currentShape < length && distance >= next ) {
						float x = lastX + next*dx*r;
						float y = lastY + next*dy*r;
						t.setToTranslation( x, y );
						t.rotate( angle );
						result.append( t.createTransformedShape( shapes[currentShape] ), false );
						next += advance;
						currentShape++;
						if ( repeat )
							currentShape %= length;
					}
				}
				next -= distance;
				first = false;
				lastX = thisX;
				lastY = thisY;
				break;
			}
			it.next();
		}

		return result;
	}

}

