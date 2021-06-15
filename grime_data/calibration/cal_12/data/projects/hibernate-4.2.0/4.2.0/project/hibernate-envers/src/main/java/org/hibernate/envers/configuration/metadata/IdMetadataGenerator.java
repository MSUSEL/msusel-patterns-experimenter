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
import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import org.hibernate.MappingException;
import org.hibernate.envers.ModificationStore;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.envers.configuration.metadata.reader.PropertyAuditingData;
import org.hibernate.envers.entities.IdMappingData;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.entities.mapper.SimpleMapperBuilder;
import org.hibernate.envers.entities.mapper.id.EmbeddedIdMapper;
import org.hibernate.envers.entities.mapper.id.MultipleIdMapper;
import org.hibernate.envers.entities.mapper.id.SimpleIdMapperBuilder;
import org.hibernate.envers.entities.mapper.id.SingleIdMapper;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * Generates metadata for primary identifiers (ids) of versions entities.
 * @author Adam Warski (adam at warski dot org)
 */
public final class IdMetadataGenerator {
    private final AuditMetadataGenerator mainGenerator;

    IdMetadataGenerator(AuditMetadataGenerator auditMetadataGenerator) {
        mainGenerator = auditMetadataGenerator;
    }

    @SuppressWarnings({"unchecked"})
    private boolean addIdProperties(Element parent, Iterator<Property> properties, SimpleMapperBuilder mapper, boolean key,
                                    boolean audited) {
        while (properties.hasNext()) {
            Property property = properties.next();
            Type propertyType = property.getType();
            if (!"_identifierMapper".equals(property.getName())) {
                boolean added = false;
                if (propertyType instanceof ManyToOneType) {
                    added = mainGenerator.getBasicMetadataGenerator().addManyToOne(parent,
                            getIdPersistentPropertyAuditingData(property),
                            property.getValue(), mapper);
                } else {
                    // Last but one parameter: ids are always insertable
                    added = mainGenerator.getBasicMetadataGenerator().addBasic(parent,
                            getIdPersistentPropertyAuditingData(property),
                            property.getValue(), mapper, true, key);
                }
                if (!added) {
                    // If the entity is audited, and a non-supported id component is used, throwing an exception.
                    // If the entity is not audited, then we simply don't support this entity, even in
                    // target relation mode not audited.
                    if (audited) {
                        throw new MappingException("Type not supported: " + propertyType.getClass().getName());
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @SuppressWarnings({"unchecked"})
    IdMappingData addId(PersistentClass pc, boolean audited) {
        // Xml mapping which will be used for relations
        Element rel_id_mapping = new DefaultElement("properties");
        // Xml mapping which will be used for the primary key of the versions table
        Element orig_id_mapping = new DefaultElement("composite-id");

        Property id_prop = pc.getIdentifierProperty();
        Component id_mapper = pc.getIdentifierMapper();

        // Checking if the id mapping is supported
        if (id_mapper == null && id_prop == null) {
            return null;
        }

        SimpleIdMapperBuilder mapper;
        if (id_mapper != null) {
            // Multiple id

            mapper = new MultipleIdMapper(((Component) pc.getIdentifier()).getComponentClassName());
            if (!addIdProperties(rel_id_mapping, (Iterator<Property>) id_mapper.getPropertyIterator(), mapper, false, audited)) {
                return null;
            }

            // null mapper - the mapping where already added the first time, now we only want to generate the xml
            if (!addIdProperties(orig_id_mapping, (Iterator<Property>) id_mapper.getPropertyIterator(), null, true, audited)) {
                return null;
            }
        } else if (id_prop.isComposite()) {
            // Embedded id

            Component id_component = (Component) id_prop.getValue();

            mapper = new EmbeddedIdMapper(getIdPropertyData(id_prop), id_component.getComponentClassName());
            if (!addIdProperties(rel_id_mapping, (Iterator<Property>) id_component.getPropertyIterator(), mapper, false, audited)) {
                return null;
            }

            // null mapper - the mapping where already added the first time, now we only want to generate the xml
            if (!addIdProperties(orig_id_mapping, (Iterator<Property>) id_component.getPropertyIterator(), null, true, audited)) {
                return null;
            }
        } else {
            // Single id
            
            mapper = new SingleIdMapper();

            // Last but one parameter: ids are always insertable
            mainGenerator.getBasicMetadataGenerator().addBasic(rel_id_mapping,
                    getIdPersistentPropertyAuditingData(id_prop),
                    id_prop.getValue(), mapper, true, false);

            // null mapper - the mapping where already added the first time, now we only want to generate the xml
            mainGenerator.getBasicMetadataGenerator().addBasic(orig_id_mapping,
                    getIdPersistentPropertyAuditingData(id_prop),
                    id_prop.getValue(), null, true, true);
        }

        orig_id_mapping.addAttribute("name", mainGenerator.getVerEntCfg().getOriginalIdPropName());

        // Adding a relation to the revision entity (effectively: the "revision number" property)
        mainGenerator.addRevisionInfoRelation(orig_id_mapping);

        return new IdMappingData(mapper, orig_id_mapping, rel_id_mapping);
    }

    private PropertyData getIdPropertyData(Property property) {
        return new PropertyData(property.getName(), property.getName(), property.getPropertyAccessorName(),
				ModificationStore.FULL);
    }

    private PropertyAuditingData getIdPersistentPropertyAuditingData(Property property) {
        return new PropertyAuditingData(property.getName(), property.getPropertyAccessorName(),
                ModificationStore.FULL, RelationTargetAuditMode.AUDITED, null, null, false);
    }
}
