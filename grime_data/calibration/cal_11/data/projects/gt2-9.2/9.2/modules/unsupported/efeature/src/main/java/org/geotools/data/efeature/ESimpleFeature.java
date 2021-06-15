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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.efeature;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.Transaction;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;

/**
 * This interface added EMF {@link EObject container} lookup to {@link SimpleFeature}.
 *  
 * @author kengu - 28. mai 2011 
 *
 *
 * @source $URL$
 */
public interface ESimpleFeature extends SimpleFeature {

    /**
     * Get EMF {@link EObject} containing the values of 
     * this {@link Feature} instance.
     * <p>
     * If {@link #eFeature()} and {@link #eObject()} is not
     * the same object instance, then {@link #eFeature()} must delegate 
     * to {@link #eObject()}.
     * <p/>
     * @return an {@link EObject} instance
     */
    public EObject eObject();
    
    /**
     * Get {@link EFeature} containing the values of 
     * this {@link Feature} instance. 
     * <p>
     * If {@link #eFeature()} and {@link #eObject()} is not
     * the same object instance, then {@link #eFeature()} must delegate 
     * to {@link #eObject()}.
     * <p/>
     * @return an {@link EFeature} instance
     */
    public EFeature eFeature();
    
    /**
     * Check if the values of this {@link Feature} is detached
     * from {@link #eObject()}.
     * @return <code>true</code> if values are detached.
     * @see {@link EFeatureHints#EFEATURE_VALUES_DETACHED}
     */
    public boolean isDetached();
    
    /**
     * Check if this {@link Feature} is a singleton.
     * @return <code>true</code> if this is a singleton.
     * @see {@link EFeatureHints#EFEATURE_SINGLETON_FEATURES}
     */
    public boolean isSingleton();
    
    /**
     * Read values from {@link #eObject()}.
     * <p>
     * This is a convenience method which forwards to 
     * {@link #write(Transaction)} using {@link Transaction#AUTO_COMMIT}.
     * </p>
     * @return list of values just read into this feature
     */
    public List<Object> read();
    
    /**
     * Read values from {@link #eObject()}.
     * @param transaction {@link Transaction} instance 
     * @return list of values just read into this feature
     */
    public List<Object> read(Transaction transaction);
    
    
    /**
     * Write values to {@link #eObject()}
     * <p>
     * This is a convenience method which forwards to 
     * {@link #write(Transaction)} using {@link Transaction#AUTO_COMMIT}.
     * </p>
     * @return list of values just written to {@link #eObject()}
     * @throws IllegalStateException If values are not {@link #isDetached() detached}
     */
    public List<Object> write() throws IllegalStateException;
    
    /**
     * Write values to {@link #eObject()}
     * @param transaction {@link Transaction} instance 
     * @return list of values just written to {@link #eObject()}
     * @throws IllegalStateException If values are not {@link #isDetached() detached}
     */
    public List<Object> write(Transaction transaction) throws IllegalStateException;

    /**
     * Release any references to {@link #eObject()}
     * <p>
     * This method releases the reference to the {@link EObject} 
     * which this {@link Feature} get it's values from.
     */
    public void release();
    
    /**
     * Check if feature has released cached reference to {@link #eObject()}
     * @return <code>true</code> if reference is released
     */
    public boolean isReleased();
        
}
