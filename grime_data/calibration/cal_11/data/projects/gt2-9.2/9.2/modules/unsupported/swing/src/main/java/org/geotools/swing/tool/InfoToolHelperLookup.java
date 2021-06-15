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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import org.geotools.map.Layer;

/**
 * A lookup facility for {@code InfoToolHelper} classes.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
class InfoToolHelperLookup {
    private static final Logger LOGGER = Logger.getLogger("org.geotools.swing");

    private static List<InfoToolHelper> cachedInstances;

    public static InfoToolHelper getHelper(Layer layer) {
        loadProviders();
        
        for (InfoToolHelper helper : cachedInstances) {
            try {
                if (helper.isSupportedLayer(layer)) {
                    return helper.getClass().newInstance();
                }
                
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }

    /**
     * Caches available classes which implement the InfoToolHelper SPI.
     */
    private static void loadProviders() {
        List<Class> providers = null;
        
        if (cachedInstances == null) {
            cachedInstances = new ArrayList<InfoToolHelper>();
            
            ServiceLoader<InfoToolHelper> loader = 
                    ServiceLoader.load(InfoToolHelper.class);
            
            Iterator<InfoToolHelper> iter = loader.iterator();
            while (iter.hasNext()) {
                cachedInstances.add(iter.next());
            }
        }
    }

}
