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
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// OpenGIS direct dependencies
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;


/**
 * A union type consisting of either a {@linkplain DirectPosition direct position} or of a
 * reference to a {@linkplain Point point} from which a {@linkplain DirectPosition direct
 * position} shall be obtained. The use of this data type allows the identification of a
 * position either directly as a coordinate (variant direct) or indirectly as a reference
 * to a {@linkplain Point point} (variant indirect).
 *  
 * @UML datatype GM_Position
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version 2.0
 */
public class PositionImpl implements Position {
    
    //*************************************************************************
    //  Fields
    //*************************************************************************
    
    private DirectPosition position;
    
    //*************************************************************************
    //  Constructor
    //*************************************************************************
    
    public PositionImpl(final DirectPosition position) {
        this.position = position;
    }
    
    //*************************************************************************
    //  implement the Position interface
    //*************************************************************************

    
    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Position#getPosition()
     */
    @Deprecated
    public DirectPosition getPosition() {
        return position;
    }
    
    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Position#getDirectPosition()
     */
    public DirectPosition getDirectPosition() {
        return position;
    }
}
