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
package org.hibernate.envers.entities;
import org.hibernate.envers.entities.mapper.PropertyMapper;
import org.hibernate.envers.entities.mapper.id.IdMapper;

/**
 * @author Adam Warski (adam at warski dot org)
*/
public class RelationDescription {
    private final String fromPropertyName;
    private final RelationType relationType;
    private final String toEntityName;
    private final String mappedByPropertyName;
    private final IdMapper idMapper;
    private final PropertyMapper fakeBidirectionalRelationMapper;
    private final PropertyMapper fakeBidirectionalRelationIndexMapper;
    private final boolean insertable;
    private boolean bidirectional;

    public RelationDescription(String fromPropertyName, RelationType relationType, String toEntityName,
                               String mappedByPropertyName, IdMapper idMapper,
                               PropertyMapper fakeBidirectionalRelationMapper,
                               PropertyMapper fakeBidirectionalRelationIndexMapper, boolean insertable) {
        this.fromPropertyName = fromPropertyName;
        this.relationType = relationType;
        this.toEntityName = toEntityName;
        this.mappedByPropertyName = mappedByPropertyName;
        this.idMapper = idMapper;
        this.fakeBidirectionalRelationMapper = fakeBidirectionalRelationMapper;
        this.fakeBidirectionalRelationIndexMapper = fakeBidirectionalRelationIndexMapper;
        this.insertable = insertable;

        this.bidirectional = false;
    }

    public String getFromPropertyName() {
        return fromPropertyName;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public String getToEntityName() {
        return toEntityName;
    }

    public String getMappedByPropertyName() {
        return mappedByPropertyName;
    }

    public IdMapper getIdMapper() {
        return idMapper;
    }

    public PropertyMapper getFakeBidirectionalRelationMapper() {
        return fakeBidirectionalRelationMapper;
    }

    public PropertyMapper getFakeBidirectionalRelationIndexMapper() {
        return fakeBidirectionalRelationIndexMapper;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public boolean isBidirectional() {
        return bidirectional;
    }

    void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }
}
