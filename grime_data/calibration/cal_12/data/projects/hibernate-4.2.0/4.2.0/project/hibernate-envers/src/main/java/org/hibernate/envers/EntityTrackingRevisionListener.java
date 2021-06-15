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
package org.hibernate.envers;

import java.io.Serializable;

/**
 * Extension of standard {@link RevisionListener} that notifies whenever an entity instance has been
 * added, modified or removed within current revision boundaries.
 * @see RevisionListener
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public interface EntityTrackingRevisionListener extends RevisionListener {
    /**
     * Called after audited entity data has been persisted.
     * @param entityClass Audited entity class.
     * @param entityName Name of the audited entity. May be useful when Java class is mapped multiple times,
     *                   potentially to different tables. 
     * @param entityId Identifier of modified entity.
     * @param revisionType Modification type (addition, update or removal).
     * @param revisionEntity An instance of the entity annotated with {@link RevisionEntity}.
     */
    void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
                       Object revisionEntity);
}
