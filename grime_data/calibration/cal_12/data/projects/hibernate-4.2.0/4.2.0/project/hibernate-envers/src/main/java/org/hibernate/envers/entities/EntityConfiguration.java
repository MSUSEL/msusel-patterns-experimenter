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
import java.util.HashMap;
import java.util.Map;

import org.hibernate.envers.entities.mapper.ExtendedPropertyMapper;
import org.hibernate.envers.entities.mapper.PropertyMapper;
import org.hibernate.envers.entities.mapper.id.IdMapper;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 */
public class EntityConfiguration {
    private String versionsEntityName;
    /** Holds the className for instantiation the configured entity */
    private String entityClassName;
	private IdMappingData idMappingData;
    private ExtendedPropertyMapper propertyMapper;
    // Maps from property name
    private Map<String, RelationDescription> relations;
    private String parentEntityName;

    public EntityConfiguration(String versionsEntityName, String entityClassName, IdMappingData idMappingData,
                               ExtendedPropertyMapper propertyMapper, String parentEntityName) {
        this.versionsEntityName = versionsEntityName;
        this.entityClassName = entityClassName;
        this.idMappingData = idMappingData;
        this.propertyMapper = propertyMapper;
        this.parentEntityName = parentEntityName;

        this.relations = new HashMap<String, RelationDescription>();
    }

    public void addToOneRelation(String fromPropertyName, String toEntityName, IdMapper idMapper, boolean insertable) {
        relations.put(fromPropertyName, new RelationDescription(fromPropertyName, RelationType.TO_ONE,
                toEntityName, null, idMapper, null, null, insertable));
    }

    public void addToOneNotOwningRelation(String fromPropertyName, String mappedByPropertyName, String toEntityName,
                                          IdMapper idMapper) {
        relations.put(fromPropertyName, new RelationDescription(fromPropertyName, RelationType.TO_ONE_NOT_OWNING,
                toEntityName, mappedByPropertyName, idMapper, null, null, true));
    }

    public void addToManyNotOwningRelation(String fromPropertyName, String mappedByPropertyName, String toEntityName,
                                           IdMapper idMapper, PropertyMapper fakeBidirectionalRelationMapper,
                                           PropertyMapper fakeBidirectionalRelationIndexMapper) {
        relations.put(fromPropertyName, new RelationDescription(fromPropertyName, RelationType.TO_MANY_NOT_OWNING,
                toEntityName, mappedByPropertyName, idMapper, fakeBidirectionalRelationMapper,
                fakeBidirectionalRelationIndexMapper, true));
    }

    public void addToManyMiddleRelation(String fromPropertyName, String toEntityName) {
        relations.put(fromPropertyName, new RelationDescription(fromPropertyName, RelationType.TO_MANY_MIDDLE,
                toEntityName, null, null, null, null, true));
    }

    public void addToManyMiddleNotOwningRelation(String fromPropertyName, String mappedByPropertyName, String toEntityName) {
        relations.put(fromPropertyName, new RelationDescription(fromPropertyName, RelationType.TO_MANY_MIDDLE_NOT_OWNING,
                toEntityName, mappedByPropertyName, null, null, null, true));
    }

    public boolean isRelation(String propertyName) {
        return relations.get(propertyName) != null;
    }
    
    public RelationDescription getRelationDescription(String propertyName) {
        return relations.get(propertyName);
    }

    public IdMappingData getIdMappingData() {
        return idMappingData;
    }

    public IdMapper getIdMapper() {
        return idMappingData.getIdMapper();
    }

    public ExtendedPropertyMapper getPropertyMapper() {
        return propertyMapper;
    }

    public String getParentEntityName() {
        return parentEntityName;
    }

    // For use by EntitiesConfigurations

    String getVersionsEntityName() {
        return versionsEntityName;
    }

    Iterable<RelationDescription> getRelationsIterator() {
        return relations.values();
    }
    
    /**
     * @return the className for the configured entity
     */
    public String getEntityClassName() {
		return entityClassName;
	}
}
