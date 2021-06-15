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
package org.hibernate.envers.synchronization.work;
import java.io.Serializable;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.envers.RevisionType;

/**
 * TODO: refactor constructors into factory methods
 * @author Adam Warski (adam at warski dot org)
 */
public interface AuditWorkUnit extends WorkUnitMergeVisitor, WorkUnitMergeDispatcher {
    Serializable getEntityId();
    String getEntityName();
    
    boolean containsWork();

    boolean isPerformed();

    /**
     * Perform this work unit in the given session.
     * @param session Session, in which the work unit should be performed.
     * @param revisionData The current revision data, which will be used to populate the work unit with the correct
     * revision relation.
     */
    void perform(Session session, Object revisionData);
    void undo(Session session);

    /**
     * @param revisionData The current revision data, which will be used to populate the work unit with the correct
     * revision relation.
     * @return Generates data that should be saved when performing this work unit.
     */
    Map<String, Object> generateData(Object revisionData);

    /**
     * @return Performed modification type.
     */
    RevisionType getRevisionType();
}
