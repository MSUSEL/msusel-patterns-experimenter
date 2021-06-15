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
package org.geotools.filter.function;

import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Given an original rendering envelope it visits an expression, finds all
 * {@link GeometryTransformation}, collects and merges all the returned query envelopes
 *
 *
 * @source $URL$
 */
public class GeometryTransformationVisitor extends DefaultFilterVisitor {

    public GeometryTransformationVisitor() {
    }

    @Override
    public Object visit(Function expression, Object data) {
        // drill down and merge
        ReferencedEnvelope merged = new ReferencedEnvelope((ReferencedEnvelope) data);
        for(Expression param : expression.getParameters()) {
            ReferencedEnvelope result = (ReferencedEnvelope) param.accept(this, data);
            if(result != null)
                merged.expandToInclude(result);
        }

        // apply the current function is possible
        if (expression instanceof GeometryTransformation) {
            merged = ((GeometryTransformation) expression).invert(merged);
        }

        return merged;
    }

}
