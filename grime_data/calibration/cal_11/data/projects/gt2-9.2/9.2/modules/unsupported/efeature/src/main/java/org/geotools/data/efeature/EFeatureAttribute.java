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

import org.opengis.feature.Attribute;
import org.opengis.feature.Property;

/**
 * Generic interface for accessing {@link Attribute feature attribute} data.
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link EFeatureAttribute#getStructure <em>Structure</em>}</li>
 * </ul>
 * </p>
 * This class extends {@link EFeatureProperty}. </p>
 * 
 * @param <V> - Actual {@link Property#getValue() property value} class.
 * 
 * @see EFeatureProperty
 * @see EFeaturePackage#getEFeatureProperty()
 * @see EFeaturePackage#getEFeatureAttribute()
 * 
 * @author kengu
 *
 * @source $URL$
 */
public interface EFeatureAttribute<V> extends EFeatureProperty<V, Attribute> {

    /**
     * Get the attribute {@link EFeatureAttributeInfo structure} instance.
     * 
     * @return the value of the '<em>Structure</em>' attribute.
     */
    @Override
    public EFeatureAttributeInfo getStructure();

} // EFeatureAttribute
