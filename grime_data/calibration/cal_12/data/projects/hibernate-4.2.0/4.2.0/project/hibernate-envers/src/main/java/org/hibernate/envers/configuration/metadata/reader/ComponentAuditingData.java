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
import java.util.Set;

import static org.hibernate.envers.tools.Tools.newHashMap;

/**
 * Audit mapping meta-data for component.
 * @author Adam Warski (adam at warski dot org)
 * @author Hern&aacut;n Chanfreau
 */
public class ComponentAuditingData extends PropertyAuditingData implements AuditedPropertiesHolder {
	private final Map<String, PropertyAuditingData> properties;

	public ComponentAuditingData() {
		this.properties = newHashMap();
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
    
    public boolean contains(String propertyName) {
    	return properties.containsKey(propertyName);
    }

	public Set<String> getPropertyNames() {
		return properties.keySet();
	}
}