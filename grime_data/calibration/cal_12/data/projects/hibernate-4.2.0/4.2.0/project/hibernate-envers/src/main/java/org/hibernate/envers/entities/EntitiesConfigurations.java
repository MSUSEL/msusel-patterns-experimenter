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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Configuration of the user entities: property mapping of the entities, relations, inheritance.
 * @author Adam Warski (adam at warski dot org)
 * @author Hern&aacute;n Chanfreau
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class EntitiesConfigurations {
    private Map<String, EntityConfiguration> entitiesConfigurations;
    private Map<String, EntityConfiguration> notAuditedEntitiesConfigurations;

    // Map versions entity name -> entity name
    private Map<String, String> entityNamesForVersionsEntityNames = new HashMap<String, String>();

	public EntitiesConfigurations(Map<String, EntityConfiguration> entitiesConfigurations,
								  Map<String, EntityConfiguration> notAuditedEntitiesConfigurations) {
        this.entitiesConfigurations = entitiesConfigurations;
        this.notAuditedEntitiesConfigurations = notAuditedEntitiesConfigurations;

        generateBidirectionRelationInfo();
        generateVersionsEntityToEntityNames();
    }

    private void generateVersionsEntityToEntityNames() {
        entityNamesForVersionsEntityNames = new HashMap<String, String>();

        for (String entityName : entitiesConfigurations.keySet()) {
            entityNamesForVersionsEntityNames.put(entitiesConfigurations.get(entityName).getVersionsEntityName(),
                    entityName);
        }
    }

    private void generateBidirectionRelationInfo() {
        // Checking each relation if it is bidirectional. If so, storing that information.
        for (String entityName : entitiesConfigurations.keySet()) {
            EntityConfiguration entCfg = entitiesConfigurations.get(entityName);
            // Iterating over all relations from that entity
            for (RelationDescription relDesc : entCfg.getRelationsIterator()) {
                // If this is an "owned" relation, checking the related entity, if it has a relation that has
                // a mapped-by attribute to the currently checked. If so, this is a bidirectional relation.
                if (relDesc.getRelationType() == RelationType.TO_ONE ||
						relDesc.getRelationType() == RelationType.TO_MANY_MIDDLE) {
					EntityConfiguration entityConfiguration = entitiesConfigurations.get(relDesc.getToEntityName());
					if (entityConfiguration != null) {
						for (RelationDescription other : entityConfiguration.getRelationsIterator()) {
							if (relDesc.getFromPropertyName().equals(other.getMappedByPropertyName()) &&
									(entityName.equals(other.getToEntityName()))) {
								relDesc.setBidirectional(true);
								other.setBidirectional(true);
							}
						}
					}
                }
            }
        }
    }

    public EntityConfiguration get(String entityName) {
        return entitiesConfigurations.get(entityName);
    }

    public EntityConfiguration getNotVersionEntityConfiguration(String entityName) {
      return notAuditedEntitiesConfigurations.get(entityName);
  }

    public String getEntityNameForVersionsEntityName(String versionsEntityName) {
        return entityNamesForVersionsEntityNames.get(versionsEntityName);
    }

    public boolean isVersioned(String entityName) {
        return get(entityName) != null;
    }

    public boolean hasAuditedEntities() {
        return entitiesConfigurations.size() != 0;
    }

    public RelationDescription getRelationDescription(String entityName, String propertyName) {
        EntityConfiguration entCfg = entitiesConfigurations.get(entityName);
        RelationDescription relDesc = entCfg.getRelationDescription(propertyName);
        if (relDesc != null) {
            return relDesc;
        } else if (entCfg.getParentEntityName() != null) {
            // The field may be declared in a superclass ...
            return getRelationDescription(entCfg.getParentEntityName(), propertyName);
        } else {
            return null;
        }
    }

	private Collection<RelationDescription> getRelationDescriptions(String entityName) {
		EntityConfiguration entCfg = entitiesConfigurations.get(entityName);
		Collection<RelationDescription> descriptions = new ArrayList<RelationDescription>();
		if (entCfg.getParentEntityName() != null) {
			// collect descriptions from super classes
			descriptions.addAll(getRelationDescriptions(entCfg.getParentEntityName()));
		}
		for (RelationDescription relationDescription : entCfg.getRelationsIterator()) {
			descriptions.add(relationDescription);
		}
		return descriptions;
	}

	private void addWithParentEntityNames(String entityName, Set<String> entityNames) {
		entityNames.add(entityName);
		EntityConfiguration entCfg = entitiesConfigurations.get(entityName);
		if (entCfg.getParentEntityName() != null) {
			// collect descriptions from super classes
			addWithParentEntityNames(entCfg.getParentEntityName(), entityNames);
		}
	}

	private Set<String> getEntityAndParentsNames(String entityName) {
		Set<String> names = new HashSet<String>();
		addWithParentEntityNames(entityName, names);
		return names;
	}

	public Set<String> getToPropertyNames(String fromEntityName, String fromPropertyName, String toEntityName) {
		Set<String> entityAndParentsNames = getEntityAndParentsNames(fromEntityName);
		Set<String> toPropertyNames = new HashSet<String>();
		for (RelationDescription relationDescription : getRelationDescriptions(toEntityName)) {
			String relToEntityName = relationDescription.getToEntityName();
			String mappedByPropertyName = relationDescription.getMappedByPropertyName();
			if (entityAndParentsNames.contains(relToEntityName) && mappedByPropertyName != null && mappedByPropertyName.equals(fromPropertyName)) {
				toPropertyNames.add(relationDescription.getFromPropertyName());
			}
		}
		return toPropertyNames;
	}
}
