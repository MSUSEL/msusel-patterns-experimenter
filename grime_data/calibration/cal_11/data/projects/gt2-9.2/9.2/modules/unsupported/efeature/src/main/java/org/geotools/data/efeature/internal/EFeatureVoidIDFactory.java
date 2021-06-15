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
package org.geotools.data.efeature.internal;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.impl.EFeatureContextImpl;
import org.geotools.data.efeature.impl.EFeatureIDFactoryImpl;

/**
 * This class implements a void {@link EFeature#getID() ID} factory.
 * <p>
 * It is only used by {@link EFeatureContextHelper} to tell the {@link EFeatureContextImpl} 
 * to not automatically create IDs after {@link EObject}s are added to 
 * registered {@link EFeatureContext#eAdd(String, org.eclipse.emf.edit.domain.EditingDomain) domains}.
 * <p>  
 * Any attempt to create or use IDs will throw and {@link UnsupportedOperationException}.
 * </p>
 * 
 * @see {@link #createID(EObject)} - will throw an {@link UnsupportedOperationException}
 * @see {@link #eCreateID(URI, EObject, EClass, EAttribute)} - will throw an {@link UnsupportedOperationException}
 * @see {@link #useID(EObject, String)} - will throw an {@link UnsupportedOperationException}
 * @see {@link #eUseID(URI, EObject, EAttribute, String, boolean)} - will throw an {@link UnsupportedOperationException}
 * 
 * @author kengu - 4. juni 2011
 * 
 *
 * @source $URL$
 */
public class EFeatureVoidIDFactory extends EFeatureIDFactoryImpl {
    
     @Override
    public String createID(EObject eObject) throws UnsupportedOperationException,
            IllegalStateException {
        throw new UnsupportedOperationException("ID creation not supported");
    }

    @Override
    public String useID(EObject eObject, String eID) throws UnsupportedOperationException,
            IllegalStateException {
        throw new UnsupportedOperationException("ID creation not supported");
    }
    
}
