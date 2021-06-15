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
package org.hibernate.envers.configuration.metadata;
import org.dom4j.Element;

import org.hibernate.MappingException;
import org.hibernate.envers.configuration.metadata.reader.PropertyAuditingData;
import org.hibernate.envers.entities.EntityConfiguration;
import org.hibernate.envers.entities.IdMappingData;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.entities.mapper.CompositeMapperBuilder;
import org.hibernate.envers.entities.mapper.id.IdMapper;
import org.hibernate.envers.entities.mapper.relation.OneToOneNotOwningMapper;
import org.hibernate.envers.entities.mapper.relation.OneToOnePrimaryKeyJoinColumnMapper;
import org.hibernate.envers.entities.mapper.relation.ToOneIdMapper;
import org.hibernate.envers.tools.MappingTools;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.Value;

/**
 * Generates metadata for to-one relations (reference-valued properties).
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public final class ToOneRelationMetadataGenerator {
    private final AuditMetadataGenerator mainGenerator;

    ToOneRelationMetadataGenerator(AuditMetadataGenerator auditMetadataGenerator) {
        mainGenerator = auditMetadataGenerator;
    }

    @SuppressWarnings({"unchecked"})
    void addToOne(Element parent, PropertyAuditingData propertyAuditingData, Value value,
                  CompositeMapperBuilder mapper, String entityName, boolean insertable) {
        String referencedEntityName = ((ToOne) value).getReferencedEntityName();

        IdMappingData idMapping = mainGenerator.getReferencedIdMappingData(entityName, referencedEntityName,
                propertyAuditingData, true);

        String lastPropertyPrefix = MappingTools.createToOneRelationPrefix(propertyAuditingData.getName());

        // Generating the id mapper for the relation
        IdMapper relMapper = idMapping.getIdMapper().prefixMappedProperties(lastPropertyPrefix);

        // Storing information about this relation
        mainGenerator.getEntitiesConfigurations().get(entityName).addToOneRelation(
                propertyAuditingData.getName(), referencedEntityName, relMapper, insertable);

        // If the property isn't insertable, checking if this is not a "fake" bidirectional many-to-one relationship,
        // that is, when the one side owns the relation (and is a collection), and the many side is non insertable.
        // When that's the case and the user specified to store this relation without a middle table (using
        // @AuditMappedBy), we have to make the property insertable for the purposes of Envers. In case of changes to
        // the entity that didn't involve the relation, it's value will then be stored properly. In case of changes
        // to the entity that did involve the relation, it's the responsibility of the collection side to store the
        // proper data.
        boolean nonInsertableFake;
        if (!insertable && propertyAuditingData.isForceInsertable()) {
            nonInsertableFake = true;
            insertable = true;
        } else {
            nonInsertableFake = false;
        }

        // Adding an element to the mapping corresponding to the references entity id's
        Element properties = (Element) idMapping.getXmlRelationMapping().clone();
        properties.addAttribute("name", propertyAuditingData.getName());

        MetadataTools.prefixNamesInPropertyElement(properties, lastPropertyPrefix,
                MetadataTools.getColumnNameIterator(value.getColumnIterator()), false, insertable);

		// Extracting related id properties from properties tag
		for (Object o : properties.content()) {
			Element element = (Element) o;
			element.setParent(null);
			parent.add(element);
		}

		// Adding mapper for the id
        PropertyData propertyData = propertyAuditingData.getPropertyData();
        mapper.addComposite(propertyData, new ToOneIdMapper(relMapper, propertyData, referencedEntityName, nonInsertableFake));
    }

    @SuppressWarnings({"unchecked"})
    void addOneToOneNotOwning(PropertyAuditingData propertyAuditingData, Value value,
                              CompositeMapperBuilder mapper, String entityName) {
        OneToOne propertyValue = (OneToOne) value;

        String owningReferencePropertyName = propertyValue.getReferencedPropertyName(); // mappedBy

        EntityConfiguration configuration = mainGenerator.getEntitiesConfigurations().get(entityName);
        if (configuration == null) {
            throw new MappingException("An audited relation to a non-audited entity " + entityName + "!");
        }

        IdMappingData ownedIdMapping = configuration.getIdMappingData();

        if (ownedIdMapping == null) {
            throw new MappingException("An audited relation to a non-audited entity " + entityName + "!");
        }

        String lastPropertyPrefix = MappingTools.createToOneRelationPrefix(owningReferencePropertyName);
        String referencedEntityName = propertyValue.getReferencedEntityName();

        // Generating the id mapper for the relation
        IdMapper ownedIdMapper = ownedIdMapping.getIdMapper().prefixMappedProperties(lastPropertyPrefix);

        // Storing information about this relation
        mainGenerator.getEntitiesConfigurations().get(entityName).addToOneNotOwningRelation(
                propertyAuditingData.getName(), owningReferencePropertyName,
                referencedEntityName, ownedIdMapper);

        // Adding mapper for the id
        PropertyData propertyData = propertyAuditingData.getPropertyData();
        mapper.addComposite(propertyData, new OneToOneNotOwningMapper(entityName, referencedEntityName,
                owningReferencePropertyName, propertyData));
    }

    @SuppressWarnings({"unchecked"})
    void addOneToOnePrimaryKeyJoinColumn(PropertyAuditingData propertyAuditingData, Value value,
                                         CompositeMapperBuilder mapper, String entityName, boolean insertable) {
        String referencedEntityName = ((ToOne) value).getReferencedEntityName();

        IdMappingData idMapping = mainGenerator.getReferencedIdMappingData(entityName, referencedEntityName,
                                                                           propertyAuditingData, true);

        String lastPropertyPrefix = MappingTools.createToOneRelationPrefix(propertyAuditingData.getName());

        // Generating the id mapper for the relation
        IdMapper relMapper = idMapping.getIdMapper().prefixMappedProperties(lastPropertyPrefix);

        // Storing information about this relation
        mainGenerator.getEntitiesConfigurations().get(entityName).addToOneRelation(propertyAuditingData.getName(),
                                                                                   referencedEntityName, relMapper, insertable);

        // Adding mapper for the id
        PropertyData propertyData = propertyAuditingData.getPropertyData();
        mapper.addComposite(propertyData, new OneToOnePrimaryKeyJoinColumnMapper(entityName, referencedEntityName, propertyData));
    }
}
