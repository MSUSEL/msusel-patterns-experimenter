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
package org.geotools.graph.build.feature;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.build.basic.BasicGraphGenerator;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Builds a graph from {@link org.geotools.feature.Feature} objects.
 * <p>
 * This graph generator decorates another graph generator which 
 * builds a graph from geometries. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @author Anders Bakkevold, Bouvet AS, bakkedev@gmail.com
 *
 * @source $URL$
 */
public class FeatureGraphGenerator extends BasicGraphGenerator {

	/**
	 * The underling "geometry" building graph generator
	 */
	GraphGenerator decorated;
	
	public FeatureGraphGenerator( GraphGenerator decorated ) {
		this.decorated = decorated;
	}
	
	public Graph getGraph() {
		return decorated.getGraph();
	}
	
	public GraphBuilder getGraphBuilder() {
		return decorated.getGraphBuilder();
	}

	public GraphGenerator getDecorated() {
            return decorated;
        }

	public Graphable add( Object obj ) {
		SimpleFeature feature = (SimpleFeature) obj;
		Graphable g = decorated.add( feature.getDefaultGeometry() );
        Geometry geom = (Geometry) g.getObject();
        //Preserve geometry from Graphable, as it may be changed.
        feature.setDefaultGeometry(geom);
        g.setObject( feature );
		return g;
	}
	
	public Graphable remove( Object obj ) {
		SimpleFeature feature = (SimpleFeature) obj;
		return decorated.remove( feature.getDefaultGeometry() );
	}
	
	public Graphable get(Object obj) {
		SimpleFeature feature = (SimpleFeature) obj;
		return decorated.get( feature.getDefaultGeometry() );
	}
}
