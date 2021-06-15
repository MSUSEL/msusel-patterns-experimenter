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
package org.geotools.data.efeature.adapters;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * An Adapter class to be used to extract from -adapt- the argument object to some {@link String}
 * value that would later be used in <code>Condition</code> evaluation.
 * 
 * Clients can subclass it and provide their own implementation
 * 
 * @see {@link Condition}
 *
 * @source $URL$
 */
public abstract class WKTAdapter implements IDataTypeAdapter<String> {

    /**
     * Cached {@link WKTWriter} instance for writing
     */
    public final static WKTWriter WRITER = new WKTWriter();

    /**
     * Cached {@link WKTReader} instance for reading
     */
    public final static WKTReader READER = new WKTReader();

    /**
     * The simplest {@link WKTAdapter} implementation that represents 
     * the case when the object to adapt is a {@link String} object or
     * a {@link Geometry} instance.
     */
    public static final WKTAdapter DEFAULT = new WKTAdapter() {
        @Override
        public String toWKT(Object object) {
            try {
                //
                // Validate?
                //
                if (object instanceof String && ((String) object).length() != 0) {
                    // Will fail if not valid geometry
                    //
                    object = READER.read((String) object);
                }
                //
                // If geometry, write to WKT
                //
                if (object instanceof Geometry) {
                    return WRITER.write((Geometry) object);
                }
            } catch (Exception e) { /* Consume */ }
            //
            // All attempts to adapt failed
            //
            throw new IllegalArgumentException("Object " + object + " is not a WKT");
        }

    };

    /**
     * Adapts given object to a WKT {@link String}
     * 
     * @param object - the object to adapt to a WKT {@link String} 
     * @return a WKT {@link String}
     */
    public abstract String toWKT(Object object);

    /**
     * Adapts given object to a WKT {@link String}
     * 
     * @param object - the object to adapt to a WKT {@link String} 
     * @return a WKT {@link String}
     */
    @Override
    public String adapt(Object value) {
        return toWKT(value);
    }

}
