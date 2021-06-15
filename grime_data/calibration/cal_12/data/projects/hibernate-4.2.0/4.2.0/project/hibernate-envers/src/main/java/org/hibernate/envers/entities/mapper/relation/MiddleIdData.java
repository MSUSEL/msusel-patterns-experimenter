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
import org.hibernate.envers.entities.IdMappingData;
import org.hibernate.envers.entities.mapper.id.IdMapper;

/**
 * A class holding information about ids, which form a virtual "relation" from a middle-table. Middle-tables are used
 * when mapping collections.
 * @author Adam Warski (adam at warski dot org)
 */
public final class MiddleIdData {
    private final IdMapper originalMapper;
    private final IdMapper prefixedMapper;
    private final String entityName;
    private final String auditEntityName;

    public MiddleIdData(AuditEntitiesConfiguration verEntCfg, IdMappingData mappingData, String prefix,
                        String entityName, boolean audited) {
        this.originalMapper = mappingData.getIdMapper();
        this.prefixedMapper = mappingData.getIdMapper().prefixMappedProperties(prefix);
        this.entityName = entityName;
        this.auditEntityName = audited ? verEntCfg.getAuditEntityName(entityName) : null;
    }

    /**
     * @return Original id mapper of the related entity.
     */
    public IdMapper getOriginalMapper() {
        return originalMapper;
    }

    /**
     * @return prefixed id mapper (with the names for the id fields that are used in the middle table) of the related entity.
     */
    public IdMapper getPrefixedMapper() {
        return prefixedMapper;
    }

    /**
     * @return Name of the related entity (regular, not audited).
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @return Audit name of the related entity.
     */
    public String getAuditEntityName() {
        return auditEntityName;
    }

    /**
     * @return Is the entity, to which this middle id data correspond, audited.
     */
    public boolean isAudited() {
        return auditEntityName != null;
    }
}
