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
package org.hibernate.envers.entities.mapper;
import java.util.Map;

import org.hibernate.envers.tools.Pair;

/**
 * Data describing the change of a single object in a persistent collection (when the object was added, removed or
 * modified in the collection).
 * @author Adam Warski (adam at warski dot org)
 */
public class PersistentCollectionChangeData {
    private final String entityName;
    private final Map<String, Object> data;
    private final Object changedElement;

    public PersistentCollectionChangeData(String entityName, Map<String, Object> data, Object changedElement) {
        this.entityName = entityName;
        this.data = data;
        this.changedElement = changedElement;
    }

    /**
     *
     * @return Name of the (middle) entity that holds the collection data.
     */
    public String getEntityName() {
        return entityName;
    }

    public Map<String, Object> getData() {
        return data;
    }

    /**
     * @return The affected element, which was changed (added, removed, modified) in the collection.
     */
    public Object getChangedElement() {
        if (changedElement instanceof Pair) {
            return ((Pair) changedElement).getSecond();
        }

        if (changedElement instanceof Map.Entry) {
            return ((Map.Entry) changedElement).getValue();
        }

        return changedElement;
    }

    /**
     * @return Index of the affected element, or {@code null} if the collection isn't indexed.
     */
    public Object getChangedElementIndex() {
        if (changedElement instanceof Pair) {
            return ((Pair) changedElement).getFirst();
        }

        if (changedElement instanceof Map.Entry) {
            return ((Map.Entry) changedElement).getKey();
        }

        return null;
    }
}
