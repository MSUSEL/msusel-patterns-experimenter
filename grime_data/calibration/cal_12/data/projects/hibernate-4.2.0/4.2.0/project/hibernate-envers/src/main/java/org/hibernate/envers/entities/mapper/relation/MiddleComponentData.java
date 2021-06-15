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
package org.hibernate.envers.entities.mapper.relation;
import org.hibernate.envers.entities.mapper.relation.component.MiddleComponentMapper;

/**
 * A data holder for a middle relation component (which is either the collection element or index):
 * - component mapper used to map the component to and from versions entities
 * - an index, which specifies in which element of the array returned by the query for reading the collection the data
 * of the component is
 * @author Adam Warski (adam at warski dot org)
 */
public final class MiddleComponentData {
    private final MiddleComponentMapper componentMapper;
    private final int componentIndex;

    public MiddleComponentData(MiddleComponentMapper componentMapper, int componentIndex) {
        this.componentMapper = componentMapper;
        this.componentIndex = componentIndex;
    }

    public MiddleComponentMapper getComponentMapper() {
        return componentMapper;
    }

    public int getComponentIndex() {
        return componentIndex;
    }
}
