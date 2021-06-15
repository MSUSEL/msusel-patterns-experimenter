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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Factory;
import org.geotools.factory.GeoTools;

/**
 * A utility class for working with FeatureCollections.
 * Provides a mechanism for obtaining a SimpleFeatureCollection instance.
 * @author  Ian Schneider
 *
 * @deprecated Use DefaultFeatureCollection
 * @source $URL$
 */
public abstract class FeatureCollections implements Factory {
  
  private static FeatureCollections instance() {
      // depend on CommonFactoryFinder's FactoryRegistry to hold singleton
      return CommonFactoryFinder.getFeatureCollections( GeoTools.getDefaultHints() );
  }
  
  /**
   * create a new SimpleFeatureCollection using the current default factory.
   * @return A SimpleFeatureCollection instance.
   * @deprecated Use new DefaultFeatureCollection(null,null)
   */
  public static SimpleFeatureCollection newCollection() {
    return instance().createCollection(); 
  }
  
  /**
   * Creates a new SimpleFeatureCollection with a particular id using the current 
   * default factory.
   * 
   * @param id The id of the feature collection.
   * 
   * @return A new SimpleFeatureCollection intsance with the specified id.
   * 
   * @since 2.4
   * @deprecated Use new DefaultFeatureCollection( id, null )
   */
  public static SimpleFeatureCollection newCollection( String id ) {
	  return instance().createCollection( id );
  }
  
  /**
   * Subclasses must implement this to return a new SimpleFeatureCollection object.
   * @return A new FeatureCollection
   */
  protected abstract SimpleFeatureCollection createCollection();
  
  /**
   * Subclasses must implement this to return a new SimpleFeatureCollection object 
   * with a particular id.
   * 
   * @param id The identification of the feature collection.
   * 
   * @return A new SimpleFeatureCollection with the specified id. 
   */
  protected abstract SimpleFeatureCollection createCollection( String id );
  
  /**
   * Returns the implementation hints. The default implementation returns en empty map.
   */
  public Map<RenderingHints.Key,Object> getImplementationHints() {
    return Collections.emptyMap();
  }  
}
