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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.envers.strategy.DefaultAuditStrategy;

import static org.hibernate.envers.tools.Tools.getProperty;

/**
 * Configuration of versions entities - names of fields, entities and tables created to store versioning information.
 * @author Adam Warski (adam at warski dot org)
 * @author Stephanie Pau at Markit Group Plc
 */
public class AuditEntitiesConfiguration {
    private final String auditTablePrefix;
    private final String auditTableSuffix;

    private final String auditStrategyName;
    private final String originalIdPropName;

    private final String revisionFieldName;
    private final String revisionNumberPath;
    private final String revisionPropBasePath;

    private final String revisionTypePropName;
    private final String revisionTypePropType;

    private final String revisionInfoEntityName;

    private final Map<String, String> customAuditTablesNames;

    private final String revisionEndFieldName;
    
    private final boolean revisionEndTimestampEnabled;
    private final String revisionEndTimestampFieldName;

    public AuditEntitiesConfiguration(Properties properties, String revisionInfoEntityName) {
        this.revisionInfoEntityName = revisionInfoEntityName;

        auditTablePrefix = getProperty(properties,
                "org.hibernate.envers.audit_table_prefix",
                "org.hibernate.envers.auditTablePrefix",
                "");
        auditTableSuffix = getProperty(properties,
                "org.hibernate.envers.audit_table_suffix", 
                "org.hibernate.envers.auditTableSuffix",
                "_AUD");

        auditStrategyName = getProperty(properties,
                "org.hibernate.envers.audit_strategy",
                "org.hibernate.envers.audit_strategy",
                DefaultAuditStrategy.class.getName());

        originalIdPropName = "originalId";

        revisionFieldName = getProperty(properties,
                "org.hibernate.envers.revision_field_name",
                "org.hibernate.envers.revisionFieldName",
                "REV");

        revisionTypePropName = getProperty(properties,
                "org.hibernate.envers.revision_type_field_name", 
                "org.hibernate.envers.revisionTypeFieldName",
                "REVTYPE");
        revisionTypePropType = "byte";

        revisionEndFieldName = getProperty(properties,
                "org.hibernate.envers.audit_strategy_validity_end_rev_field_name",
                "org.hibernate.envers.audit_strategy_valid_time_end_name",
                "REVEND");

        String revisionEndTimestampEnabledStr = getProperty(properties,
        		"org.hibernate.envers.audit_strategy_validity_store_revend_timestamp",
        		"org.hibernate.envers.audit_strategy_validity_store_revend_timestamp",
        		"false");
        revisionEndTimestampEnabled = Boolean.parseBoolean(revisionEndTimestampEnabledStr);
                
        if (revisionEndTimestampEnabled) {
            revisionEndTimestampFieldName = getProperty(properties,
            		"org.hibernate.envers.audit_strategy_validity_revend_timestamp_field_name",
            		"org.hibernate.envers.audit_strategy_validity_revend_timestamp_field_name",
            		"REVEND_TSTMP");
        } else {
            revisionEndTimestampFieldName = null;
        }
        
        customAuditTablesNames = new HashMap<String, String>();

        revisionNumberPath = originalIdPropName + "." + revisionFieldName + ".id";
        revisionPropBasePath = originalIdPropName + "." + revisionFieldName + ".";
    }

    public String getOriginalIdPropName() {
        return originalIdPropName;
    }

    public String getRevisionFieldName() {
        return revisionFieldName;
    }

	public boolean isRevisionEndTimestampEnabled() {
		return revisionEndTimestampEnabled;
	}

	public String getRevisionEndTimestampFieldName() {
		return revisionEndTimestampFieldName;
	}
    
    public String getRevisionNumberPath() {
        return revisionNumberPath;
    }

    /**
     * @param propertyName Property of the revision entity.
     * @return A path to the given property of the revision entity associated with an audit entity.
     */
    public String getRevisionPropPath(String propertyName) {
        return revisionPropBasePath + propertyName;
    }

    public String getRevisionTypePropName() {
        return revisionTypePropName;
    }

    public String getRevisionTypePropType() {
        return revisionTypePropType;
    }

    public String getRevisionInfoEntityName() {
        return revisionInfoEntityName;
    }

    //

    public void addCustomAuditTableName(String entityName, String tableName) {
        customAuditTablesNames.put(entityName, tableName);
    }

    //

    public String getAuditEntityName(String entityName) {
        return auditTablePrefix + entityName + auditTableSuffix;
    }

    public String getAuditTableName(String entityName, String tableName) {
        String customHistoryTableName = customAuditTablesNames.get(entityName);
        if (customHistoryTableName == null) {
            return auditTablePrefix + tableName + auditTableSuffix;
        }

        return customHistoryTableName;
    }

    public String getAuditStrategyName() {
        return auditStrategyName;
    }

    public String getRevisionEndFieldName() {
        return revisionEndFieldName;
    }
}
