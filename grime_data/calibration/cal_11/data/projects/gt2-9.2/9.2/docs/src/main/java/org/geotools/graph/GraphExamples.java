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
package org.geotools.graph;

import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.line.BasicLineGraphBuilder;
import org.geotools.graph.build.line.BasicLineGraphGenerator;
import org.geotools.graph.build.line.LineGraphGenerator;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.SimpleGraphWalker;
import org.geotools.graph.traverse.standard.BreadthFirstIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;

public class GraphExamples {
void graphExample() throws Exception {
    SimpleFeatureSource featureSource = null;
    
    // graphExample start
    final LineGraphGenerator generator = new BasicLineGraphGenerator();
    SimpleFeatureCollection fc = featureSource.getFeatures();
    
    fc.accepts(new FeatureVisitor() {
        public void visit(Feature feature) {
            generator.add(feature);
        }
    }, null);
    Graph graph = generator.getGraph();
    // graphExample end
    
    // visitor example start
    class OrphanVisitor implements GraphVisitor {
        private int count = 0;
        public int getCount() {
            return count;
        }
        public int visit(Graphable component) {
            Iterator related = component.getRelated();
            if( related.hasNext() == false ){
                // no related components makes this an orphan
                count++;
            }
            return GraphTraversal.CONTINUE;
        }
    }
    OrphanVisitor graphVisitor = new OrphanVisitor();
    
    SimpleGraphWalker sgv = new SimpleGraphWalker(graphVisitor);
    GraphIterator iterator = new BreadthFirstIterator();
    BasicGraphTraversal bgt = new BasicGraphTraversal(graph, sgv, iterator);
    
    bgt.traverse();
    
    System.out.println("Found orphans: " + graphVisitor.getCount());
    // visitor example end
}
}
