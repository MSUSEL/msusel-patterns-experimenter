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
package org.hibernate.envers.revisioninfo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionListener;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.synchronization.SessionCacheCleaner;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.Setter;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class DefaultRevisionInfoGenerator implements RevisionInfoGenerator {
    private final String revisionInfoEntityName;
    private final RevisionListener listener;
    private final Setter revisionTimestampSetter;
    private final boolean timestampAsDate;
    private final Class<?> revisionInfoClass;
    private final SessionCacheCleaner sessionCacheCleaner;

    public DefaultRevisionInfoGenerator(String revisionInfoEntityName, Class<?> revisionInfoClass,
                                       Class<? extends RevisionListener> listenerClass,
                                       PropertyData revisionInfoTimestampData,
                                       boolean timestampAsDate) {
        this.revisionInfoEntityName = revisionInfoEntityName;
        this.revisionInfoClass = revisionInfoClass;
        this.timestampAsDate = timestampAsDate;

        revisionTimestampSetter = ReflectionTools.getSetter(revisionInfoClass, revisionInfoTimestampData);

        if (!listenerClass.equals(RevisionListener.class)) {
            // This is not the default value.
            try {
                listener = (RevisionListener) ReflectHelper.getDefaultConstructor(listenerClass).newInstance();
            } catch (InstantiationException e) {
                throw new MappingException(e);
            } catch (IllegalAccessException e) {
                throw new MappingException(e);
            } catch (InvocationTargetException e) {
                throw new MappingException(e);
            }
        } else {
            // Default listener - none
            listener = null;
        }

        sessionCacheCleaner = new SessionCacheCleaner();
    }

	public void saveRevisionData(Session session, Object revisionData) {
        session.save(revisionInfoEntityName, revisionData);
        sessionCacheCleaner.scheduleAuditDataRemoval(session, revisionData);
	}

    public Object generate() {
		Object revisionInfo;
        try {
            revisionInfo = ReflectHelper.getDefaultConstructor(revisionInfoClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long timestamp = System.currentTimeMillis();
        revisionTimestampSetter.set(revisionInfo, timestampAsDate ? new Date(timestamp) : timestamp, null);

        if (listener != null) {
            listener.newRevision(revisionInfo);
        }

        return revisionInfo;
    }

    public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
                              Object revisionInfo) {
        if (listener instanceof EntityTrackingRevisionListener) {
            ((EntityTrackingRevisionListener) listener).entityChanged(entityClass, entityName, entityId, revisionType,
                                                                      revisionInfo);
        }
    }
}
