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
import java.lang.annotation.Annotation;
import java.util.Iterator;

import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.ModificationStore;
import org.hibernate.envers.SecondaryAuditTable;
import org.hibernate.envers.SecondaryAuditTables;
import org.hibernate.envers.configuration.GlobalConfiguration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

/**
 * A helper class to read versioning meta-data from annotations on a persistent class.
 * @author Adam Warski (adam at warski dot org)
 * @author Sebastian Komander
 */
public final class AnnotationsMetadataReader {
	private final GlobalConfiguration globalCfg;
	private final ReflectionManager reflectionManager;
	private final PersistentClass pc;

	/**
	 * This object is filled with information read from annotations and returned by the <code>getVersioningData</code>
	 * method.
	 */
	private final ClassAuditingData auditData;

	public AnnotationsMetadataReader(GlobalConfiguration globalCfg, ReflectionManager reflectionManager,
									 PersistentClass pc) {
		this.globalCfg = globalCfg;
		this.reflectionManager = reflectionManager;
		this.pc = pc;

		auditData = new ClassAuditingData();
	}

	private ModificationStore getDefaultAudited(XClass clazz) {
		Audited defaultAudited = clazz.getAnnotation(Audited.class);

		if (defaultAudited != null) {
			return defaultAudited.modStore();
		} else {
			return null;
		}
	}

	private void addAuditTable(XClass clazz) {
		AuditTable auditTable = clazz.getAnnotation(AuditTable.class);
		if (auditTable != null) {
			auditData.setAuditTable(auditTable);
		} else {
			auditData.setAuditTable(getDefaultAuditTable());
		}
	}

	private void addAuditSecondaryTables(XClass clazz) {
		// Getting information on secondary tables
		SecondaryAuditTable secondaryVersionsTable1 = clazz.getAnnotation(SecondaryAuditTable.class);
		if (secondaryVersionsTable1 != null) {
			auditData.getSecondaryTableDictionary().put(secondaryVersionsTable1.secondaryTableName(),
					secondaryVersionsTable1.secondaryAuditTableName());
		}

		SecondaryAuditTables secondaryAuditTables = clazz.getAnnotation(SecondaryAuditTables.class);
		if (secondaryAuditTables != null) {
			for (SecondaryAuditTable secondaryAuditTable2 : secondaryAuditTables.value()) {
				auditData.getSecondaryTableDictionary().put(secondaryAuditTable2.secondaryTableName(),
						secondaryAuditTable2.secondaryAuditTableName());
			}
		}
	}

	public ClassAuditingData getAuditData() {
		if (pc.getClassName() == null) {
			return auditData;
		}

		try {
			XClass xclass = reflectionManager.classForName(pc.getClassName(), this.getClass());

			ModificationStore defaultStore = getDefaultAudited(xclass);
			if (defaultStore != null) {
				auditData.setDefaultAudited(true);
			}

			new AuditedPropertiesReader(defaultStore, new PersistentClassPropertiesSource(xclass), auditData,
					globalCfg, reflectionManager, "").read();

			addAuditTable(xclass);
			addAuditSecondaryTables(xclass);
		} catch (ClassNotFoundException e) {
			throw new MappingException(e);
		}

		return auditData;
	}

	private AuditTable defaultAuditTable = new AuditTable() {
		public String value() { return ""; }
		public String schema() { return ""; }
		public String catalog() { return ""; }
		public Class<? extends Annotation> annotationType() { return this.getClass(); }
	};

	private AuditTable getDefaultAuditTable() {
		return defaultAuditTable;
	}

	private class PersistentClassPropertiesSource implements PersistentPropertiesSource {
		private final XClass xclass;

		private PersistentClassPropertiesSource(XClass xclass) { this.xclass = xclass; }

		@SuppressWarnings({"unchecked"})
		public Iterator<Property> getPropertyIterator() { return pc.getPropertyIterator(); }
		public Property getProperty(String propertyName) { return pc.getProperty(propertyName); }
		public XClass getXClass() { return xclass; }
	}
}
