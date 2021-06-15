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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Topological sorting of a graph - based on DFS.
 * @author Adam Warski (adam at warski dot org)
 */
public class TopologicalSort<R> {
    private List<R> sorted;
    private int time;

    private void process(Vertex<R> v) {
        if (v.getStartTime() != 0) {
            // alread processed
            return;
        }

        v.setStartTime(time++);

        for (Vertex<R> n : v.getNeighbours()) {
            process(n);
        }

        v.setEndTime(time++);

        sorted.add(v.getRepresentation());
    }

    public List<R> sort(Collection<Vertex<R>> vertices) {
        sorted = new ArrayList<R>(vertices.size());
        
        time = 1;

        for (Vertex<R> v : vertices) {
            if (v.getEndTime() == 0) {
                process(v);
            }
        }

        Collections.reverse(sorted);

        return sorted;
    }
}
