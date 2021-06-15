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
package org.geotools.renderer.shape;

import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * 
 *
 * @source $URL$
 */
public class GeometryFilterChecker extends DefaultFilterVisitor {
	
	boolean geometryFilterPresent = false;
	
	public boolean isGeometryFilterPresent() {
		return geometryFilterPresent;
	}

	@Override
	public Object visit(BBOX filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Beyond filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Contains filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Crosses filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Disjoint filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(DWithin filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Equals filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Intersects filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Overlaps filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Touches filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Within filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
}
