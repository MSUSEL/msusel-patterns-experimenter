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
package org.hibernate.envers.configuration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.envers.tools.Tools;
import org.hibernate.envers.tools.graph.GraphDefiner;
import org.hibernate.mapping.PersistentClass;

/**
 * Defines a graph, where the vertexes are all persistent classes, and there is an edge from
 * p.c. A to p.c. B iff A is a superclass of B.
 * @author Adam Warski (adam at warski dot org)
 */
public class PersistentClassGraphDefiner implements GraphDefiner<PersistentClass, String> {
    private Configuration cfg;

    public PersistentClassGraphDefiner(Configuration cfg) {
        this.cfg = cfg;
    }

    public String getRepresentation(PersistentClass pc) {
        return pc.getEntityName();
    }

    public PersistentClass getValue(String entityName) {
        return cfg.getClassMapping(entityName);
    }

    @SuppressWarnings({"unchecked"})
    private void addNeighbours(List<PersistentClass> neighbours, Iterator<PersistentClass> subclassIterator) {
        while (subclassIterator.hasNext()) {
            PersistentClass subclass = subclassIterator.next();
            neighbours.add(subclass);
            addNeighbours(neighbours, (Iterator<PersistentClass>) subclass.getSubclassIterator());
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<PersistentClass> getNeighbours(PersistentClass pc) {
        List<PersistentClass> neighbours = new ArrayList<PersistentClass>();

        addNeighbours(neighbours, (Iterator<PersistentClass>) pc.getSubclassIterator());

        return neighbours;
    }

    @SuppressWarnings({"unchecked"})
    public List<PersistentClass> getValues() {
        return Tools.iteratorToList((Iterator<PersistentClass>) cfg.getClassMappings());
    }
}
