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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.MappingException;
import org.hibernate.envers.configuration.metadata.reader.ClassAuditingData;
import org.hibernate.envers.configuration.metadata.reader.PropertyAuditingData;
import org.hibernate.envers.internal.EnversMessageLogger;
import org.hibernate.envers.tools.MappingTools;
import org.hibernate.mapping.PersistentClass;

/**
 * A helper class holding auditing meta-data for all persistent classes.
 * @author Adam Warski (adam at warski dot org)
 */
public class ClassesAuditingData {

    public static final EnversMessageLogger LOG = Logger.getMessageLogger(EnversMessageLogger.class, ClassesAuditingData.class.getName());

    private final Map<String, ClassAuditingData> entityNameToAuditingData = new HashMap<String, ClassAuditingData>();
    private final Map<PersistentClass, ClassAuditingData> persistentClassToAuditingData = new LinkedHashMap<PersistentClass, ClassAuditingData>();

    /**
     * Stores information about auditing meta-data for the given class.
     * @param pc Persistent class.
     * @param cad Auditing meta-data for the given class.
     */
    public void addClassAuditingData(PersistentClass pc, ClassAuditingData cad) {
        entityNameToAuditingData.put(pc.getEntityName(), cad);
        persistentClassToAuditingData.put(pc, cad);
    }

    /**
     * @return A collection of all auditing meta-data for persistent classes.
     */
    public Collection<Map.Entry<PersistentClass, ClassAuditingData>> getAllClassAuditedData() {
        return persistentClassToAuditingData.entrySet();
    }

    /**
     * @param entityName Name of the entity.
     * @return Auditing meta-data for the given entity.
     */
    public ClassAuditingData getClassAuditingData(String entityName) {
        return entityNameToAuditingData.get(entityName);
    }

    /**
     * After all meta-data is read, updates calculated fields. This includes:
     * <ul>
     * <li>setting {@code forceInsertable} to {@code true} for properties specified by {@code @AuditMappedBy}</li>
     * </ul>
     */
    public void updateCalculatedFields() {
        for (Map.Entry<PersistentClass, ClassAuditingData> classAuditingDataEntry : persistentClassToAuditingData.entrySet()) {
            PersistentClass pc = classAuditingDataEntry.getKey();
            ClassAuditingData classAuditingData = classAuditingDataEntry.getValue();
            for (String propertyName : classAuditingData.getPropertyNames()) {
                PropertyAuditingData propertyAuditingData = classAuditingData.getPropertyAuditingData(propertyName);
                // If a property had the @AuditMappedBy annotation, setting the referenced fields to be always insertable.
                if (propertyAuditingData.getAuditMappedBy() != null) {
                    String referencedEntityName = MappingTools.getReferencedEntityName(pc.getProperty(propertyName).getValue());

                    ClassAuditingData referencedClassAuditingData = entityNameToAuditingData.get(referencedEntityName);

                    forcePropertyInsertable(referencedClassAuditingData, propertyAuditingData.getAuditMappedBy(),
                            pc.getEntityName(), referencedEntityName);

                    forcePropertyInsertable(referencedClassAuditingData, propertyAuditingData.getPositionMappedBy(),
                            pc.getEntityName(), referencedEntityName);
                }
            }
        }
    }

    private void forcePropertyInsertable(ClassAuditingData classAuditingData, String propertyName,
                                         String entityName, String referencedEntityName) {
        if (propertyName != null) {
            if (classAuditingData.getPropertyAuditingData(propertyName) == null) {
                throw new MappingException("@AuditMappedBy points to a property that doesn't exist: " +
                    referencedEntityName + "." + propertyName);
            }

            LOG.debugf("Non-insertable property %s.%s will be made insertable because a matching @AuditMappedBy was found in the %s entity",
                       referencedEntityName,
                       propertyName,
                       entityName);

            classAuditingData
                    .getPropertyAuditingData(propertyName)
                    .setForceInsertable(true);
        }
    }
}
