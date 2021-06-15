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
import java.util.HashSet;
import java.util.Set;

import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionListener;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;

/**
 * Automatically adds entity names, that have been changed during current revision, to revision entity.
 * @see ModifiedEntityNames
 * @see DefaultTrackingModifiedEntitiesRevisionEntity
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class DefaultTrackingModifiedEntitiesRevisionInfoGenerator extends DefaultRevisionInfoGenerator {
    private final Setter modifiedEntityNamesSetter;
    private final Getter modifiedEntityNamesGetter;

    public DefaultTrackingModifiedEntitiesRevisionInfoGenerator(String revisionInfoEntityName, Class<?> revisionInfoClass,
                                                                Class<? extends RevisionListener> listenerClass,
                                                                PropertyData revisionInfoTimestampData, boolean timestampAsDate,
                                                                PropertyData modifiedEntityNamesData) {
        super(revisionInfoEntityName, revisionInfoClass, listenerClass, revisionInfoTimestampData, timestampAsDate);
        modifiedEntityNamesSetter = ReflectionTools.getSetter(revisionInfoClass, modifiedEntityNamesData);
        modifiedEntityNamesGetter = ReflectionTools.getGetter(revisionInfoClass, modifiedEntityNamesData);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
                              Object revisionEntity) {
        super.entityChanged(entityClass, entityName, entityId, revisionType, revisionEntity);
        Set<String> modifiedEntityNames = (Set<String>) modifiedEntityNamesGetter.get(revisionEntity);
        if (modifiedEntityNames == null) {
            modifiedEntityNames = new HashSet<String>();
            modifiedEntityNamesSetter.set(revisionEntity, modifiedEntityNames, null);
        }
        modifiedEntityNames.add(entityName);
    }
}
