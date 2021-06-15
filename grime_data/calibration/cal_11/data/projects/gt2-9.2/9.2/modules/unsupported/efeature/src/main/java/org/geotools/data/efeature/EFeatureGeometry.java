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
package org.geotools.data.efeature;

import com.vividsolutions.jts.geom.Geometry;

import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;

/**
 * Generic interface for accessing {@link GeometryAttribute feature geometry} data.
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link EFeatureGeometry#isEmpty <em>Empty</em>}</li>
 * <li>{@link EFeatureGeometry#isDefaultGeometry <em>Default</em>}</li>
 * <li>{@link EFeatureGeometry#getStructure <em>Structure</em>}</li>
 * </ul>
 * </p>
 * 
 * @param <V> - Actual {@link Property#getValue() property value} class.
 * 
 * @see EFeatureAttribute
 * @see EFeaturePackage#getEFeatureAttribute()
 * @see EFeaturePackage#getEFeatureGeometry()
 * 
 * @author kengu
 *
 * @source $URL$
 */
public interface EFeatureGeometry<V extends Geometry> extends
        EFeatureProperty<V, GeometryAttribute> {

    /**
     * Check if the {@link Property#getValue() feature geometry value} is {@link Geometry#isEmpty()
     * empty}
     * 
     * @return the value of the '<em>Default</em>' attribute.
     */
    boolean isEmpty();

    /**
     * Check if this is the {@link Feature#getDefaultGeometryProperty() default feature geometry}
     * 
     * @return the value of the '<em>Default</em>' attribute.
     */
    boolean isDefault();

    /**
     * Get the attribute {@link EFeatureGeometryInfo structure} instance.
     * 
     * @return the value of the '<em>Structure</em>' attribute.
     */
    @Override
    public EFeatureGeometryInfo getStructure();

} // EGeometry
