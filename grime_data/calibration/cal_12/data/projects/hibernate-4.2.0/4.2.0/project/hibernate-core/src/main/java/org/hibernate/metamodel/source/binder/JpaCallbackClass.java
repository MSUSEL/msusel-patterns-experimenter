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
package org.hibernate.metamodel.source.binder;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public interface JpaCallbackClass {

    /**
     * @param callbackType {@link PrePersist}, {@link PreRemove}, {@link PreUpdate}, {@link PostLoad},
     *        {@link PostPersist}, {@link PostRemove}, or {@link PostUpdate}
     * @return the name of the JPA callback method defined for the associated {@link Entity entity} or {@link MappedSuperclass
     *         mapped superclass} and for the supplied callback annotation class.
     */
    String getCallbackMethod(Class<?> callbackType);

    /**
     * @return the name of the instantiated container where the JPA callbacks for the associated {@link Entity entity} or
     *         {@link MappedSuperclass mapped superclass} are defined. This can be either the entity/mapped superclass itself or an
     *         {@link EntityListeners entity listener}.
     */
    String getName();

    /**
     * @return <code>true</code> if this callback class represents callbacks defined within an {@link EntityListeners entity
     *         listener}.
     */
    boolean isListener();
}
