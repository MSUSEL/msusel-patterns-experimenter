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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.network;

import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.graph.build.line.LineStringGraphGenerator;
import org.geotools.graph.structure.Graph;
import org.geotools.validation.DefaultIntegrityValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;


/**
 * OrphanNodeValidation purpose.
 * 
 * <p>
 * Builds a network, and looks for orphaned nodes.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class OrphanNodeValidation extends DefaultIntegrityValidation {
    /** the SimpleFeatureSource name datastoreId:typename */
    private String typeName;

    /**
     * StarNodeValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public OrphanNodeValidation() {
        super();
    }
    
    /**
     * Implementation of getTypeRefs.
     * 
     * @see org.geotools.validation.Validation#getTypeRefs()
     * 
     */
    public String[] getTypeRefs() {
    	return new String[] {typeName};
    }

    /**
     * Check FeatureType for ...
     * 
     * <p>
     * Detailed description...
     * </p>
     *
     * @param layers Map of SimpleFeatureSource by "dataStoreID:typeName"
     * @param envelope The bounding box that encloses the unvalidated data
     * @param results Used to coallate results information
     *
     * @return <code>true</code> if all the features pass this test.
     *
     * @throws Exception DOCUMENT ME!
     */
    public boolean validate(Map layers, ReferencedEnvelope envelope,
        final ValidationResults results) throws Exception {
    	
      LineStringGraphGenerator lgb = new LineStringGraphGenerator();
      SimpleFeatureSource fs = (SimpleFeatureSource) layers.get(typeName);
      SimpleFeatureCollection fr = fs.getFeatures();
      SimpleFeatureCollection fc = fr;
      SimpleFeatureIterator f = fc.features();

      while (f.hasNext()) {
          SimpleFeature ft = f.next();

          if (envelope.contains(ft.getBounds())) {
              //lgb.add(ft);
          	lgb.add(ft.getDefaultGeometry());
          }
      }

      // lgb is loaded
      Graph g = lgb.getGraph();
			
      return(g.getNodesOfDegree(0).size() == 0);
      
    }
    
//    public boolean validate_old(Map layers, Envelope envelope,
//        final ValidationResults results) throws Exception {
//        LineGraphBuilder lgb = new LineGraphBuilder();
//        SimpleFeatureSource fs = (SimpleFeatureSource) layers.get(typeName);
//        FeatureResults fr = fs.getFeatures();
//        SimpleFeatureCollection fc = fr.collection();
//        SimpleFeatureIterator f = fc.features();
//
//        while (f.hasNext()) {
//            Feature ft = f.next();
//
//            if (envelope.contains(ft.getBounds())) {
//                lgb.add(ft);
//            }
//        }
//
//        // lgb is loaded
//        org.geotools.graph.structure.Graph g = lgb.build();
//
//        class OrphanVisitor implements GraphVisitor {
//            private int count = 0;
//
//            public int getCount() {
//                return count;
//            }
//
//            public int visit(GraphComponent element) {
//                if (element.getAdjacentElements().size() == 0) {
//                    count++;
//                }
//
//                results.error(element.getFeature(), "Orphaned");
//
//                return GraphTraversal.CONTINUE;
//            }
//        }
//
//        OrphanVisitor ov = new OrphanVisitor();
//        SimpleGraphWalker sgv = new SimpleGraphWalker(ov);
//        BasicGraphTraversal bgt = new BasicGraphTraversal(g, sgv);
//        bgt.walkNodes();
//
//        if (ov.getCount() == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * Access typeName property.
     *
     * @return Returns the typeName.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Set typeName to typeName.
     *
     * @param typeName The typeName to set.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
