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
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.envers.Audited;
import org.hibernate.envers.ModificationStore;
import org.hibernate.envers.configuration.GlobalConfiguration;

/**
 * Reads the audited properties for components.
 * 
 * @author Hern&aacut;n Chanfreau
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public class ComponentAuditedPropertiesReader extends AuditedPropertiesReader {

	public ComponentAuditedPropertiesReader(ModificationStore defaultStore,
			PersistentPropertiesSource persistentPropertiesSource,
			AuditedPropertiesHolder auditedPropertiesHolder,
			GlobalConfiguration globalCfg, ReflectionManager reflectionManager,
			String propertyNamePrefix) {
		super(defaultStore, persistentPropertiesSource, auditedPropertiesHolder,
				globalCfg, reflectionManager, propertyNamePrefix);
	}
	
	@Override
	protected boolean checkAudited(XProperty property,
			PropertyAuditingData propertyData, Audited allClassAudited) {
		// Checking if this property is explicitly audited or if all properties are.
		Audited aud = property.getAnnotation(Audited.class);
		if (aud != null) {
			propertyData.setStore(aud.modStore());
			propertyData.setRelationTargetAuditMode(aud.targetAuditMode());
			propertyData.setUsingModifiedFlag(checkUsingModifiedFlag(aud));
		} else {
			propertyData.setStore(ModificationStore.FULL);
		}	
	   return true;	
	}

}
