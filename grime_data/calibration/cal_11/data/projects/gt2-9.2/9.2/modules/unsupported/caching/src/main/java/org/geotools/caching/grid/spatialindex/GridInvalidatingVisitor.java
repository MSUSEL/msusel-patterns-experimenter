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
package org.geotools.caching.grid.spatialindex;

import org.geotools.caching.spatialindex.Data;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Visitor;

/**
 * Visitor that invalidates nodes
 * 
 * @author Emily
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class GridInvalidatingVisitor implements Visitor {
	
    private Region region;
    private GridSpatialIndex index;

    /**
     * Creates a new Invalidating Visitor.
     * 
     * @param r the region to invalid nodes within
     */
    public GridInvalidatingVisitor(GridSpatialIndex index, Region r) {
        this.region = r;
        this.index = index;
    }
    
    /**
     * Creates a new Invalidating Visitor that will invalidate
     * all nodes visited (no matter where they are).
     */
    public GridInvalidatingVisitor(GridSpatialIndex index) {
    	this(index, null);
    }
    
    public boolean isDataVisitor() {
        return false;
    }

    public void visitData(Data<?> d) {
        // do nothing
    }
    
    public void visitNode(Node n) {
		if (this.region == null || this.region.contains(n.getShape())) {
			//clear & write out the node
			//note this will not clear children; in this case
			//we assume the validation will visit these children
			//and clear them separately
			n.clear();
			index.writeNode(n);
		}
	}
}
