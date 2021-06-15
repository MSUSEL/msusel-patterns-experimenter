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
package org.hibernate.cfg;

import java.util.Set;

import org.hibernate.internal.util.xml.XmlDocument;

/**
 * Represents a mapping queued for delayed processing to await
 * processing of an extends entity upon which it depends.
 *
 * @author Steve Ebersole
 */
public class ExtendsQueueEntry {
	private final String explicitName;
	private final String mappingPackage;
	private final XmlDocument metadataXml;
	private final Set<String> entityNames;

	public ExtendsQueueEntry(String explicitName, String mappingPackage, XmlDocument metadataXml, Set<String> entityNames) {
		this.explicitName = explicitName;
		this.mappingPackage = mappingPackage;
		this.metadataXml = metadataXml;
		this.entityNames = entityNames;
	}

	public String getExplicitName() {
		return explicitName;
	}

	public String getMappingPackage() {
		return mappingPackage;
	}

	public XmlDocument getMetadataXml() {
		return metadataXml;
	}

	public Set<String> getEntityNames() {
		return entityNames;
	}
}
