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
import java.util.Map;

import org.hibernate.envers.AuditTable;

import static org.hibernate.envers.tools.Tools.newHashMap;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Sebastian Komander
 * @author Hern&aacut;n Chanfreau
*/
public class ClassAuditingData implements AuditedPropertiesHolder {
    private final Map<String, PropertyAuditingData> properties;
    private final Map<String, String> secondaryTableDictionary;

    private AuditTable auditTable;

	/**
	 * True if the class is audited globally (this helps to cover the cases when there are no fields in the class,
	 * but it's still audited).
	 */
	private boolean defaultAudited;

    public ClassAuditingData() {
        properties = newHashMap();
        secondaryTableDictionary = newHashMap();
    }

    public boolean isEmpty() {
    	return properties.isEmpty();
    }

	public void addPropertyAuditingData(String propertyName, PropertyAuditingData auditingData) {
		properties.put(propertyName, auditingData);
	}

    public PropertyAuditingData getPropertyAuditingData(String propertyName) {
        return properties.get(propertyName);
    }

    public Iterable<String> getPropertyNames() {
        return properties.keySet();
    }

    public Map<String, String> getSecondaryTableDictionary() {
        return secondaryTableDictionary;
    }

    public AuditTable getAuditTable() {
        return auditTable;
    }

    public void setAuditTable(AuditTable auditTable) {
        this.auditTable = auditTable;
    }

	public void setDefaultAudited(boolean defaultAudited) {
		this.defaultAudited = defaultAudited;
	}

	public boolean isAudited() {
        return defaultAudited || properties.size() > 0;
    }
	
	public boolean contains(String propertyName) {
		return properties.containsKey(propertyName);
	}
}
