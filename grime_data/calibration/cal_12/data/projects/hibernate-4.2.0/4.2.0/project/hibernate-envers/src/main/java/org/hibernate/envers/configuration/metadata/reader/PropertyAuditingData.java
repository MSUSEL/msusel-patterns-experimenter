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
package org.hibernate.envers.configuration.metadata.reader;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.ModificationStore;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.envers.entities.PropertyData;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class PropertyAuditingData {
    private String name;
	private String beanName;
    private ModificationStore store;
    private String mapKey;
    private AuditJoinTable joinTable;
    private String accessType;
    private final List<AuditOverride> auditJoinTableOverrides = new ArrayList<AuditOverride>(0);
	private RelationTargetAuditMode relationTargetAuditMode;
    private String auditMappedBy;
    private String relationMappedBy;
    private String positionMappedBy;
    private boolean forceInsertable;
	private boolean usingModifiedFlag;
	private String modifiedFlagName;

	public PropertyAuditingData() {
    }

    public PropertyAuditingData(String name, String accessType, ModificationStore store,
								RelationTargetAuditMode relationTargetAuditMode,
                                String auditMappedBy, String positionMappedBy,
                                boolean forceInsertable) {
        this.name = name;
		this.beanName = name;
        this.accessType = accessType;
        this.store = store;
		this.relationTargetAuditMode = relationTargetAuditMode;
        this.auditMappedBy = auditMappedBy;
        this.positionMappedBy = positionMappedBy;
        this.forceInsertable = forceInsertable;
    }

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public ModificationStore getStore() {
        return store;
    }

    public void setStore(ModificationStore store) {
        this.store = store;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public AuditJoinTable getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(AuditJoinTable joinTable) {
        this.joinTable = joinTable;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public PropertyData getPropertyData() {
		return new PropertyData(name, beanName, accessType, store,
				usingModifiedFlag, modifiedFlagName);
    }

	public List<AuditOverride> getAuditingOverrides() {
		return auditJoinTableOverrides;
	}

    public String getAuditMappedBy() {
        return auditMappedBy;
    }

    public void setAuditMappedBy(String auditMappedBy) {
        this.auditMappedBy = auditMappedBy;
    }

    public String getRelationMappedBy() {
        return relationMappedBy;
    }

    public void setRelationMappedBy(String relationMappedBy) {
        this.relationMappedBy = relationMappedBy;
    }

    public String getPositionMappedBy() {
        return positionMappedBy;
    }

    public void setPositionMappedBy(String positionMappedBy) {
        this.positionMappedBy = positionMappedBy;
    }

    public boolean isForceInsertable() {
        return forceInsertable;
    }

    public void setForceInsertable(boolean forceInsertable) {
        this.forceInsertable = forceInsertable;
    }

	public boolean isUsingModifiedFlag() {
		return usingModifiedFlag;
	}

	public void setUsingModifiedFlag(boolean usingModifiedFlag) {
		this.usingModifiedFlag = usingModifiedFlag;
	}

	public void setModifiedFlagName(String modifiedFlagName) {
		this.modifiedFlagName = modifiedFlagName;
	}

	public void addAuditingOverride(AuditOverride annotation) {
		if (annotation != null) {
			String overrideName = annotation.name();
			boolean present = false;
			for (AuditOverride current : auditJoinTableOverrides) {
				if (current.name().equals(overrideName)) {
					present = true;
					break;
				}
			}
			if (!present) {
				auditJoinTableOverrides.add(annotation);
			}
		}
	}

	public void addAuditingOverrides(AuditOverrides annotationOverrides) {
		if (annotationOverrides != null) {
			for (AuditOverride annotation : annotationOverrides.value()) {
				addAuditingOverride(annotation);
			}
		}
	}

	/**
	 * Get the relationTargetAuditMode property.
	 *
	 * @return the relationTargetAuditMode property value
	 */
	public RelationTargetAuditMode getRelationTargetAuditMode() {
		return relationTargetAuditMode;
	}

	/**
	 * Set the relationTargetAuditMode property value.
	 *
	 * @param relationTargetAuditMode the relationTargetAuditMode to set
	 */
	public void setRelationTargetAuditMode(RelationTargetAuditMode relationTargetAuditMode) {
		this.relationTargetAuditMode = relationTargetAuditMode;
	}

}
