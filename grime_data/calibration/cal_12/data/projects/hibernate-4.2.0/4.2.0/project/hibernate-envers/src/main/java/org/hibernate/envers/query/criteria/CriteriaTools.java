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
package org.hibernate.envers.query.criteria;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.RelationDescription;
import org.hibernate.envers.entities.RelationType;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.property.PropertyNameGetter;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class CriteriaTools {
    private CriteriaTools() { }

    public static void checkPropertyNotARelation(AuditConfiguration verCfg, String entityName,
                                                 String propertyName) throws AuditException {
        if (verCfg.getEntCfg().get(entityName).isRelation(propertyName)) {
            throw new AuditException("This criterion cannot be used on a property that is " +
                    "a relation to another property.");
        }
    }

    public static RelationDescription getRelatedEntity(AuditConfiguration verCfg, String entityName,
                                                       String propertyName) throws AuditException {
        RelationDescription relationDesc = verCfg.getEntCfg().getRelationDescription(entityName, propertyName);

        if (relationDesc == null) {
            return null;
        }

        if (relationDesc.getRelationType() == RelationType.TO_ONE) {
            return relationDesc;
        }

        throw new AuditException("This type of relation (" + entityName + "." + propertyName +
                ") isn't supported and can't be used in queries.");
    }

	/**
	 * @see #determinePropertyName(AuditConfiguration, AuditReaderImplementor, String, String)
	 */
	public static String determinePropertyName(AuditConfiguration auditCfg, AuditReaderImplementor versionsReader,
											   String entityName, PropertyNameGetter propertyNameGetter) {
		return determinePropertyName( auditCfg, versionsReader, entityName, propertyNameGetter.get( auditCfg ) );
	}

	/**
	 * @param auditCfg Audit configuration.
	 * @param versionsReader Versions reader.
	 * @param entityName Original entity name (not audited).
	 * @param propertyName Property name or placeholder.
	 * @return Path to property. Handles identifier placeholder used by {@link AuditId}.
	 */
	public static String determinePropertyName(AuditConfiguration auditCfg, AuditReaderImplementor versionsReader,
											   String entityName, String propertyName) {
		if ( AuditId.IDENTIFIER_PLACEHOLDER.equals( propertyName ) ) {
			final String identifierPropertyName = versionsReader.getSessionImplementor().getFactory().getEntityPersister( entityName ).getIdentifierPropertyName();
			propertyName = auditCfg.getAuditEntCfg().getOriginalIdPropName() + "." + identifierPropertyName;
		}
		return propertyName;
	}
}
