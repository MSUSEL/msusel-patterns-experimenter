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
package org.hibernate.envers.tools.graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class GraphTopologicalSort {
    /**
     * Sorts a graph topologically.
     * @param definer Defines a graph (values and representations) to sort.
     * @return Values of the graph, sorted topologically.
     */
    public static <V, R> List<V> sort(GraphDefiner<V, R> definer) {
        List<V> values = definer.getValues();
        Map<R, Vertex<R>> vertices = new HashMap<R, Vertex<R>>();

        // Creating a vertex for each representation
        for (V v : values) {
            R rep = definer.getRepresentation(v);
            vertices.put(rep, new Vertex<R>(rep));
        }

        // Connecting neighbourhooding vertices
        for (V v : values) {
            for (V vn : definer.getNeighbours(v)) {
                vertices.get(definer.getRepresentation(v)).addNeighbour(vertices.get(definer.getRepresentation(vn)));
            }
        }

        // Sorting the representations
        List<R> sortedReps = new TopologicalSort<R>().sort(vertices.values());

        // Transforming the sorted representations to sorted values 
        List<V> sortedValues = new ArrayList<V>(sortedReps.size());
        for (R rep : sortedReps) {
            sortedValues.add(definer.getValue(rep));
        }

        return sortedValues;
    }
}
