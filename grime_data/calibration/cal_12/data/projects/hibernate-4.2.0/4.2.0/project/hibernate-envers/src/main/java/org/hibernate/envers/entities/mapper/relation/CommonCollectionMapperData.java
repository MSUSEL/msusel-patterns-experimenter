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
import org.hibernate.envers.configuration.AuditEntitiesConfiguration;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.entities.mapper.relation.query.RelationQueryGenerator;

/**
 * Data that is used by all collection mappers, regardless of the type.  
 * @author Adam Warski (adam at warski dot org)
 */
public final class CommonCollectionMapperData {
    private final AuditEntitiesConfiguration verEntCfg;
    private final String versionsMiddleEntityName;
    private final PropertyData collectionReferencingPropertyData;
    private final MiddleIdData referencingIdData;
    private final RelationQueryGenerator queryGenerator;

    public CommonCollectionMapperData(AuditEntitiesConfiguration verEntCfg, String versionsMiddleEntityName,
                                      PropertyData collectionReferencingPropertyData, MiddleIdData referencingIdData,
                                      RelationQueryGenerator queryGenerator) {
        this.verEntCfg = verEntCfg;
        this.versionsMiddleEntityName = versionsMiddleEntityName;
        this.collectionReferencingPropertyData = collectionReferencingPropertyData;
        this.referencingIdData = referencingIdData;
        this.queryGenerator = queryGenerator;
    }

    public AuditEntitiesConfiguration getVerEntCfg() {
        return verEntCfg;
    }

    public String getVersionsMiddleEntityName() {
        return versionsMiddleEntityName;
    }

    public PropertyData getCollectionReferencingPropertyData() {
        return collectionReferencingPropertyData;
    }

    public MiddleIdData getReferencingIdData() {
        return referencingIdData;
    }

    public RelationQueryGenerator getQueryGenerator() {
        return queryGenerator;
    }
}
