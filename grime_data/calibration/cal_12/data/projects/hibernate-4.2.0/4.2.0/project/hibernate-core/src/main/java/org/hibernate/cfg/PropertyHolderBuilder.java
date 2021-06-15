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
import java.util.Map;

import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.cfg.annotations.EntityBinder;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.PersistentClass;

/**
 * This factory is here ot build a PropertyHolder and prevent .mapping interface adding
 *
 * @author Emmanuel Bernard
 */
public final class PropertyHolderBuilder {
	private PropertyHolderBuilder() {
	}

	public static PropertyHolder buildPropertyHolder(
			XClass clazzToProcess,
			PersistentClass persistentClass,
			EntityBinder entityBinder,
			Mappings mappings,
			Map<XClass, InheritanceState> inheritanceStatePerClass) {
		return new ClassPropertyHolder(
				persistentClass, clazzToProcess, entityBinder, mappings, inheritanceStatePerClass
		);
	}

	/**
	 * build a component property holder
	 *
	 * @param component component to wrap
	 * @param path	  component path
	 * @param mappings
	 * @return PropertyHolder
	 */
	public static PropertyHolder buildPropertyHolder(
			Component component,
			String path,
			PropertyData inferredData,
			PropertyHolder parent,
			Mappings mappings) {
		return new ComponentPropertyHolder( component, path, inferredData, parent, mappings );
	}

	/**
	 * buid a property holder on top of a collection
	 */
	public static PropertyHolder buildPropertyHolder(
			Collection collection,
			String path,
			XClass clazzToProcess,
			XProperty property,
			PropertyHolder parentPropertyHolder,
			Mappings mappings) {
		return new CollectionPropertyHolder(
				collection, path, clazzToProcess, property, parentPropertyHolder, mappings
		);
	}

	/**
	 * must only be used on second level phases (<join> has to be settled already)
	 */
	public static PropertyHolder buildPropertyHolder(
			PersistentClass persistentClass,
			Map<String, Join> joins,
			Mappings mappings,
			Map<XClass, InheritanceState> inheritanceStatePerClass) {
		return new ClassPropertyHolder( persistentClass, null, joins, mappings, inheritanceStatePerClass );
	}
}
